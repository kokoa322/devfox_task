
1. MySql의 limit 과 offset 을 활용한 간결 기본적인 페이징.
- 간결하고 가독성이 좋습니다.
특징은 게시글 목록에서 99번째 페이지 도달시 1000개의 게시글까지 조회가 되는데 1000의 모든 속성의 레코드(글 내용, 수정날짜, 글번호 등..)를 조회하여 DB의 리소스에 부담이됩니다.
  
SELECT
    b.id, <--- 실제 필요한 레코드
    b.title, <--- ''
    b.created_at, <--- ''
    u.username <--- ''
FROM boards AS b  <-- 이부분에서 boards 의 모든레코드를 조회합니다
INNER JOIN users AS u
ON u.id = b.user_id
ORDER BY b.id DESC
LIMIT 10 OFFSET 99;

2. '1'의 단점을 보완한 MySql쿼리입니다
- 레코드 조회시 디스크I/O 줄이기 위한 서브쿼리
특징은 from절에서 필요한 속성만 셀렉트하여  불필요한 레코드를 줄여줍니다.
그렇지만 아직도 1000개의 게시물을 조회한다면 ... "4개의 속성(id, user_id, title, created_at) * 1000 = 4000"개의 레코드를 조회하게 됩니다,
SELECT
    b.id,
    b.title,
    b.created_at,
    u.username
FROM (
    SELECT id, user_id, title, created_at 
    FROM boards
    ORDER BY id DESC
    LIMIT 10 OFFSET 99
) AS b
INNER JOIN (
    SELECT id, username
    FROM users
) AS u
ON u.id = b.user_id;

3. “2”의 방법에서 개선한 WHIT 사용한 쿼리 (WHIT 이란? 복잡한 서브쿼리를 인라인 뷰 처럼 사용 할수 있게 해주는 SQL 명령어)
- 1000번째 게시물까지 도달 할때  user_id와 board_id 두개의 속성만 조회하여 마지막에 한 번만 모든 컬럼을 조회합니다.
단점은 두가지가 있습니다.
첫번째
-"1"의 방법의 비해서 코드가 복잡합니다. (가독성 낮음)
두번째
- 가까운 페이지 번호는 1, 2번 방법이 속도가 빠릅니다.
왜?) 1, 2번 방법에 비해서 실행 명령어가 많기 때문입니다, 하지만 미세한 속도차이라서 상관없습니다.(약 4ms 차이)


-- 1단계: 보드 ID와 유저 ID만 조인
WITH board_users AS (
    SELECT b.id AS board_id, b.user_id 
    FROM (
        SELECT id, user_id
        FROM boards
        ORDER BY id DESC
        LIMIT 10 OFFSET 99
    ) AS b
    INNER JOIN users AS u
    ON u.id = b.user_id
)

-- 2단계: 최종적으로 필요한 컬럼 조회
SELECT
    b.id,
    b.title,
    b.created_at,
    u.username
FROM board_users bu
INNER JOIN boards b ON bu.board_id = b.id
INNER JOIN users u ON bu.user_id = u.id;

DB 마이그레이션을 위해, MySql의 페이징 처리 방법을 오라클 쿼리로 수정 하면서 공부해봤습니다.

1. MySql의 "1"의 방법의 기본적인 페이징을 "Oracle 쿼리"로 수정.
방법은 "두가지"가 있습니다. 
1-1.  ROW_NUMBER()를 활용한 페이징. 
단점 - ROW_NUMBER()가 모든 데이터에 번호를 생성하는 과정 때문에 데이터 양이 만아질수록 성능 저하가 심합니다.
-- ROW_NUMBER()로 부여한 번호로 페이징처리를 하기 위해 먼저 서브 쿼리를 사용했습니다.
SELECT * FROM (
    SELECT
        b.id,
        b.title,
        b.created_at,
        u.username,
	  	-- ROW_NUMBER() 각 행의 번호 부여
        ROW_NUMBER() OVER (ORDER BY b.id DESC) AS row_num ROW_NUMBER()
    FROM boards b
    INNER JOIN users u
    ON u.id = b.user_id
)
-- ROW_NUMBER()로 부여한 번호를 바탕으로 BETWEEN사용하여 지정된 두 값 사이의 범위의 데이터를 조회합니다.
WHERE row_num BETWEEN 0 + 1 AND 0 + 10;

1-2. Oracle12 부터 지원하는 offset 페이징 
-- 문법이 간결함이 장점입니다.
SELECT
    b.id,
    b.title,
    b.created_at,
    u.username
FROM boards b
INNER JOIN users u
ON u.id = b.user_id
ORDER BY b.id DESC
-- offset을 사용하여 페이징, mysql 페이징과 비슷합니다.
OFFSET 0 ROWS FETCH NEXT 10 ROWS ONLY; 

---------------- MySql, Oracle의 Offset을 활용한 페이징 비교 ----------------------


 딥시크와 챗 지피티를 사용한 결론 (MySql 쪽의 설명은 정확합니다)
   MySQL vs Oracle 성능 차이

MySQL은 전체 결과 집합을 스캔하여 OFFSET만큼 데이터를 건너뛰기 때문에 데이터 양이 많을 경우 성능 저하가 발생할 수 있습니다.
Oracle은 FETCH 방식을 사용하여 필요한 데이터만 읽어들여 결과를 처리하는 방식으로 동작하여, OFFSET이 큰 경우에도 성능 저하를 덜 발생시킬 수 있습니다. Oracle은 쿼리 성능을 최적화하기 위해 내부적으로 효율적인 방식을 사용합니다
평소라면 Oracle도 실행 계획과 레코드 조회하는 걸 확인하면서 공부 했겠지만 현재 환경구축이 안되있어 확인을 못했습니다.

다음은 외국 커뮤니티의 개발자의 코멘트( "https://forums.oracle.com/ords/apexds/post/rownum-or-fetch-offset-is-good-to-select-and-process-millio-2695" )
(한국어로 검색할때 오라클의 오프셋 동작 원리에 대한 글이 없었습니다.)
the OFFSET clause is internally rewritten into a query useing the analytic function ROW_NUMBER() OVER (ORDER BY ...) and then a filter upon the result of this. So first the database applies a number for all rows , then it filters upon that number.

This is extremly inefficient if you want to do it for a large number of rows.

아주 간단히 설명한다면 "offset방식의 처리방법은 많은 데이터를 처리할때 비효율적" 이라는 코멘트이기 때문에 양쪽 의견 불일치 했습니다.

제가 직접 실행계획과 레코드의 조회를 확인하면서 사실확인이 필요합니다만, 현재 oracle의 환경 이슈로 직접 확인 불가능 했습니다.


2. "3"의 MySql 쿼리를 Oracle 쿼리로 수정
-- 1단계: 보드 ID와 유저 ID만 조인
WITH board_users AS (
    SELECT b.id AS board_id, b.user_id
    FROM (
        SELECT id, user_id
        FROM boards
        ORDER BY id DESC
        FETCH FIRST 10 ROWS ONLY OFFSET 99 ROWS
    ) b
    INNER JOIN users u ON u.id = b.user_id
)

-- 2단계: 최종적으로 필요한 컬럼 조회
SELECT
    b.id,
    b.title,
    b.created_at,
    u.username
FROM board_users bu
INNER JOIN boards b ON bu.board_id = b.id
INNER JOIN users u ON bu.user_id = u.id;





다음은 "Oracle의 검색 페이징에서 어떤 조인수행이 적절 한가?"에 대해서 공부하고 고민했습니다.




코드를 보면 LIKE '%' || #{searchKeyword} || '%' 키워드는 인덱스 데이터 정렬을 해봤자 의미없기 때문에 무조건 "Full Table Scan"이 일어납니다.
여기서 쿼리를 실행하면 오라클의 옵티마이져는 조인에 대해서"실행 계획"을 세웁니다. 
MySql은 오직 "Nested Loop Join"을 사용하지만 오라클은 "총 3가지 (Nested Loop Join, Sort Merge Join, Hash Join)"를 지원합니다.
여기서 검색 쿼리에 어떤 조인이 적합한가 생각했습니다.

Nested Loop Join (검색 쿼리의 특징상 인덱스를 못타기 때문에 제외) -> X
- 작은 테이블먼저 탐색후 큰 테이블을 반복 조회. ("outer table A"의 한 행을 읽고, inner table B의 인덱스를 사용해 데이터를 조회한다, 이과정을 반복하며 수행)
- 인덱스가 있을때 효율적이지만 반환 데이터가 많을수록 비효율적 (MySql 기준으로 테이블에 존제하는 레코드 70%이상 반환이 되는걸로 판단하면 풀테이블 스켄으로 넘어감)

Hash Join(동등조인이 아니라면, 그리고 중복된 값이 많을 때 속도가 Sort join 보다 느리기에 제외) -> X
- 작은 테이블을 해시테이블로 변환 후, 큰 테이블과 비교하여 조인합니다.
해시테이블로 변환하는 과정에 메모리(PGA)에 올라갑니다. 해당 메모리의 용량을 넘어가면 디스크의 용량을 사용하기 때문에 주의가 필요합니다.
- 인덱스가 없어도 효율적 입니다. 
- 대용량 데이터에 효율적입니다.
- 동등 조건에서 사용이됩니다.(부등호 조인이 적을때)

Sort Merge Join(중복데이터가 많다면 속도가 느리지만 그나마 적합하다고 판단.)  -> O 
- 두테이블을 정렬(PGA 에서 정렬된 데이터가 올라감) 하고 병합하여 조인합니다.
- 인덱스가 없거나 정렬이 필요할때 사용합니다.
- 대용량 데이터 처리에 적합 합니다.

최종적인 코드
SELECT /*+ USE_MERGE(b u) ORDERED */ 
    b.id,
    b.title,
    b.created_at,
    u.username
FROM (SELECT /*+ FULL(b) */ * FROM boards b WHERE ROWNUM <= :scale + :pNum) b
INNER JOIN (SELECT /*+ FULL(u) */ * FROM users u) u
    ON u.id = b.user_id
WHERE
    (
        CASE
            WHEN :searchType = 'title' THEN
                b.title LIKE '%' || :searchKeyword || '%'
            WHEN :searchType = 'username' THEN
                u.username LIKE '%' || :searchKeyword || '%'
            WHEN :searchType = 'content' THEN
                b.content LIKE '%' || :searchKeyword || '%'
            ELSE
                1=1
        END
    )
ORDER BY b.id DESC
FETCH FIRST :scale ROWS ONLY OFFSET :pNum ROWS;

--------------------------------------------------------------------------------------------------------------
참고 링크
 
조인 수행, 실행 계획
- 1. https://www.youtube.com/watch?v=SVD5ldwVYpo
- 2. https://www.youtube.com/watch?v=pJWCwfv983Q&t=390s
- 3. https://www.youtube.com/watch?v=Rew5va-5zAQ
- 4. https://www.youtube.com/watch?v=-u1jVjVcWWk
- 5. https://memories95.tistory.com/178
- 6. https://escapefromcoding.tistory.com/781
...

Oracle Sql
- 7. 생활코딩 Oracle 강의
- 8. https://programmer93.tistory.com/4
- 9. https://aljjabaegi.tistory.com/190
- 10. https://d4emon.tistory.com/144
- 11. https://yudaeng.tistory.com/24
- 12. https://gent.tistory.com/404
- 13. https://wakestand.tistory.com/280
...
