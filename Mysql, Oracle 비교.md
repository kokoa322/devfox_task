## MySQL과 Oracle의 페이징 처리 방법 비교

## 1. MySQL의 기본적인 페이징 처리

### 1.1 기본적인 LIMIT과 OFFSET을 사용한 페이징

- **특징**: 간결하고 가독성이 좋음. 
- **문제점**: 게시글 목록에서 99번째 페이지에 도달할 때 1000개의 게시글까지 조회되며, 이때 1000개의 게시글에 대한 모든 속성(글 내용, 수정날짜 등)을 조회하여 DB 리소스에 부담이 됨.

<p align="center">
  <img src="https://github.com/user-attachments/assets/f2037a76-90c3-453b-9ceb-673c1505ed2a" width="60%">
</p>

---

### 1.2 서브쿼리를 사용하여 디스크 I/O 줄이기

- **특징**: FROM 절에서 필요한 속성만 선택하여 불필요한 레코드를 줄여줌.
- **문제점**: 1000개의 게시물을 조회하는 경우, 각 레코드의 속성(id, user_id, title, created_at)을 조회하게 되어 여전히 많은 레코드를 다뤄야 함.

<p align="center">
  <img src="https://github.com/user-attachments/assets/b78ea79d-0868-423f-b7a9-24480b5fddaa" width="60%">
</p>

---

### 1.3 WHIT를 사용한 쿼리 개선

- **특징**: 1000번째 게시물까지 도달할 때, user_id와 board_id 두 개의 속성만 조회하고, 마지막에 모든 컬럼을 조회함.
- **단점**:
  1. **복잡성**: 첫 번째 방법에 비해 코드가 복잡하고 가독성이 떨어짐.
  2. **속도 차이**: 가까운 페이지에서 첫 번째와 두 번째 방법이 빠르지만, 이 방법은 실행 명령어가 많아 성능이 약간 저하됨. 하지만 속도 차이는 미미함.(약 4ms 차이).

<p align="center">
  <img src="https://github.com/user-attachments/assets/0dbd76f6-bc99-46ae-ab6c-eeb33a115159" width="60%">
</p>
<p align="center">
  <img src="https://github.com/user-attachments/assets/dfbc0499-7175-470d-b2f2-dcfe0d388927" width="60%">
</p>

---

## 2. Oracle에서의 페이징 처리

### 2.1 ROW_NUMBER()를 활용한 페이징

- **단점**: ROW_NUMBER()는 모든 데이터에 번호를 매기기 때문에 데이터 양이 많을수록 성능 저하가 발생할 수 있음.

<p align="center">
  <img src="https://github.com/user-attachments/assets/4ff82be2-a4a6-47b2-b78c-b30551b213ee" width="60%">
</p>

---

### 2.2 Oracle 12 이상에서 지원하는 OFFSET 페이징

- **특징**: OFFSET을 사용한 페이징 처리.

<p align="center">
  <img src="https://github.com/user-attachments/assets/2fdb089f-8c6a-46d9-860d-2f6970642bc6" width="60%">
</p>

---

## 3. MySQL과 Oracle의 Offset 페이징 비교(딥시크와 챗 지피티의 의견 입니다. 확실치 않습니다, 직접 EXPLAIN으로 실행계획과 레코드의 조회를 확인해야 하지만 현재 환경이 갖춰져있지 않기때문에 확인 못했습니다.)

- **MySQL**: 전체 결과 집합을 스캔하여 OFFSET만큼 데이터를 건너뛰기 때문에 데이터 양이 많을 경우 성능 저하가 발생할 수 있습니다(이 MySql의 오프셋 방식은 정확합니다!)
- **Oracle**: FETCH 방식을 사용하여 필요한 데이터만 읽어들이기 때문에 OFFSET이 크더라도 성능 저하가 덜 발생합니다. 내부적으로 더 효율적인 방식으로 쿼리 성능을 최적화합니다.(확인해야할 필요가 있음.)

---

## 4. 외국 커뮤니티의 의견

### Oracle의 오프셋 동작 원리에 대한 글

- **출처**: [Oracle Forum - ROWNUM or FETCH OFFSET for processing millions of rows](https://forums.oracle.com/ords/apexds/post/rownum-or-fetch-offset-is-good-to-select-and-process-millio-2695)

- **코멘트 원본**: "the OFFSET clause is internally rewritten into a query useing the analytic function ROW_NUMBER() OVER (ORDER BY ...) and then a filter upon the result of this. So first the database applies a number for all rows , then it filters upon that number.

This is extremly inefficient if you want to do it for a large number of rows."
- **내용**: OFFSET 방식은 많은 데이터를 처리할 때 비효율적이라는 의견, OFFSET은 내부적으로 ROW_NUMBER()를 사용, 성능이 떨어질 수 있음. (딥시크와 의견 불일치.)

---

## 결론

- **MySQL**: OFFSET 방식은 성능이 떨어질 수 있지만, 간단한 구조에서는 사용하기 좋음.
- **Oracle**: FETCH 방식을 사용하여 성능을 최적화할 수 있습니다. 대용량 데이터 처리에서 더 효율적일 수 있음.(확실하지 않음 직접 확인이 필요함!)

---

## "3"의 MySql 쿼리를 Oracle 쿼리로 수정

### Oracle 쿼리 예시

<p align="center">
  <img src="https://github.com/user-attachments/assets/c5486a4d-10bb-4348-aef2-d01a7240dd64" width="60%">
  <img width="622" alt="스크린샷 2025-02-02 09 39 10" src="https://github.com/user-attachments/assets/0c4a4e63-d9a4-4d09-a4e8-f52876a01bf7" />
</p>

---

### "Oracle의 검색 페이징에서 어떤 조인수행이 적절 한가?"에 대해서 공부하고 고민함.
- LIKE '%' || #{searchKeyword} || '%' 키워드는 인덱스 데이터 정렬을 해봤자 의미없기 때문에 무조건 "Full Table Scan"이 일어남.
쿼리를 실행하면 오라클의 옵티마이져는 조인에 대해서"실행 계획"을 세움. 
MySql은 오직 "Nested Loop Join"을 사용하지만 오라클은 "총 3가지 (Nested Loop Join, Sort Merge Join, Hash Join)"를 지원.
여기서 검색 쿼리에 어떤 조인이 적합한가 생각.

  1. **Nested Loop Join**: Nested Loop Join (검색 쿼리의 특징상 인덱스를 못타기 때문에 제외) -> X
- 작은 테이블먼저 탐색후 큰 테이블을 반복 조회. ("outer table A"의 한 행을 읽고, inner table B의 인덱스를 사용해 데이터를 조회한다, 이과정을 반복하며 수행)
- 인덱스가 있을때 효율적이지만 반환 데이터가 많을수록 비효율적 (MySql 기준으로 테이블에 존제하는 레코드 70%이상 반환이 되는걸로 판단하면 풀테이블 스켄으로 넘어감)

  2. **Hash Join**: Hash Join(동등조인이 아니라면, 그리고 중복된 값이 많을 때 속도가 Sort join 보다 느리기에 제외) -> X
- 작은 테이블을 해시테이블로 변환 후, 큰 테이블과 비교하여 조인함.
해시테이블로 변환하는 과정에 메모리(PGA)에 올라감. 해당 메모리의 용량을 넘어가면 디스크의 용량을 사용하기 때문에 주의가 필요함.
- 인덱스가 없어도 효율적. 
- 대용량 데이터에 효율적.
- 동등 조건에서 사용이됨.(부등호 조인이 적을때)
  
  3. **Sort Merge Join**: Sort Merge Join(중복데이터가 많다면 속도가 느리지만 그나마 적합하다고 판단.)  -> O 
- 두테이블을 정렬(PGA 에서 정렬된 데이터가 올라감) 하고 병합하여 조인함.
- 인덱스가 없거나 정렬이 필요할때 사용함.
- 대용량 데이터 처리에 적합 함.
### **적합한 조인 방식**: **Sort Merge Join으로 판단**

<p align="center">
  <img src="https://github.com/user-attachments/assets/7447b2ae-76b3-4320-8cf0-6b5c815f7da1" width="60%">
  
</p>


---

### 힌트란?

SQL 쿼리에서 옵티마이저에게 특정 실행 계획을 선택하도록 유도하는 지시어

조인 방식을 유도하는 힌트들.

| 힌트 | 설명 |
|:---:|:---:|
| USE_NL(table1 table2) | Nested Loop Join 사용 강제 |
| USE_MERGE(table1 table2) | Sort-Merge Join 사용 강제 |
| USE_HASH(table1 table2) | Hash Join 사용 강제 |
| ORDERED | FROM 절 순서대로 조인 강제 |
| LEADING(table1 table2) | 조인의 선행 테이블 지정 (먼저 읽을 테이블을 결정) |
| DRIVING_SITE(table) | 원격 조인의 실행 위치를 테이블이 있는 DB에서 실행하도록 강제 |

FULL(table_alias) 힌트란?
<img width="581" alt="스크린샷 2025-02-02 10 08 30" src="https://github.com/user-attachments/assets/3f1f809d-374a-44f0-a3ba-5583dc4181b2" />

 인덱스를 사용하지 않고 전체 테이블을 스캔(Full Table Scan, FTS) 하도록 지시하는 힌트
---

## 참고 링크

### 조인 수행, 실행 계획
- 1. [YouTube - 조인 관련](https://www.youtube.com/watch?v=SVD5ldwVYpo)
- 2. [YouTube - 조인 관련](https://www.youtube.com/watch?v=pJWCwfv983Q&t=390s)
- 3. [YouTube - 조인 관련](https://www.youtube.com/watch?v=Rew5va-5zAQ)
- 4. [YouTube - 조인 관련](https://www.youtube.com/watch?v=-u1jVjVcWWk)
- 5. [Tistory - 조인 관련](https://memories95.tistory.com/178)
- 6. [Tistory - 조인 관련](https://escapefromcoding.tistory.com/781)

### Oracle SQL
- 7. [생활코딩 Oracle 강의](https://programmer93.tistory.com/4)
- 8. [Tistory - Oracle](https://aljjabaegi.tistory.com/190)
- 9. [Tistory - Oracle](https://d4emon.tistory.com/144)
- 10. [Tistory - Oracle](https://yudaeng.tistory.com/24)
- 11. [Tistory - Oracle](https://gent.tistory.com/404)
- 12. [Tistory - Oracle](https://wakestand.tistory.com/280)
