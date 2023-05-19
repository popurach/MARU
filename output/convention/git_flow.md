# 1. Git Flow

---

<aside>
💡 Git flow는 git을 사용하는 대규모 프로젝트에서 사용하기 위해 개발된 `브랜치 관리 전략`입니다.

</aside>

## 1.1 기본 개념

Git Flow는 이전의 단순한 브랜치 전략에서 브랜치를 추가하여 더욱 세분화된 브랜치 전략으로, 다음과 같은 주요 브랜치로 이루어져 있습니다.

***주요 브랜치***

**메인 브랜치**

- `**master` (메인)**
    - 배포 가능한 안정 버전의 코드가 있는  브랜치
- `**develop` (메인)**
    - 다음 배포 버전을 개발하는 브랜치
    - 평소에는 이 브랜치를 기반으로 개발 진행
- ````````**frontend```````` (메인)**
    - 프론트엔드 다음 배포 버전을 개발하는 브랜치
    - 평소에는 이 브랜치를 기반으로 개발 진행
    - 배포할 필요가 없어 구분
-

**보조 브랜치**

- `**feature`** (보조)
    - **새로운 기능 개발**을 위한 브랜치
- `**release`** (보조)
    - develop 브랜치에서 배포를 위한 버그 수정과 최종 테스트를 수행하는 브랜치
- `**hotfix`** (보조)
    - 배포된 버전에서 긴급하게 수정이 필요한 버그를 수정하는 브랜치

즉, Git-Flow 는 master 와 develop `2개의 메인 브랜치`와 feature, release, hotfix 등 `보조 브랜치`로 이루어져 있습니다.

![pasted image 0.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/ca9602a6-5982-4e97-ba36-afc1fdb33739/pasted_image_0.png)

![pasted image 0 (1).png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/0a71da8d-dd85-4619-875b-45900c37d1fa/pasted_image_0_(1).png)

## 1.2 Git Flow 장단점

***장점***

1. 프로젝트의 구조화
    - 기능 개발, 버그 수정 등을 분리하여 프로젝트를 구조화하고 브랜치를 통해 일관성 있는 작업을 수행할 수 있습니다.
2. 쉬운 협업
    - Git Flow는 브랜치를 통해 각자 작업을 하고, 최종적으로 develop 브랜치에 merge하여 협업을 용이하게 합니다.
3. 안정성 보장
    - master 브랜치는 항상 배포 가능한 버전의 코드이므로 안정성을 보장합니다.

***단점***

1. 복잡한 통합
    - 여러 브랜치가 사용되므로 다른 전략에 비해 상대적으로 복잡합니다.
2. 브랜치의 충돌
    - 브랜치를 크게 가져가면 충돌이 발생할 가능성이 높아지므로, 적절한 크기로 유지하는 것이 중요합니다.

# 2. Convention

---

Git-Flow 를 위한 상세 규칙은 아래와 같습니다.

1. develop 브랜치로부터 feature 브랜치를 생성합니다. feature 브랜치의 경우, featrue/브랜치명 형식으로 작성합니다.
2. feature 브랜치에서 기능 개발이 완료되면 develop 브랜치로 merge 합니다.
    - **브랜치 merge 전에는 pull 명령어로 최신 코드를 가져옵니다.**
3. feature 브랜치에서 `pull request` 를 통해 코드 리뷰를 받고 merge 를 완료합니다.
4. merge 가 완료된 브랜치는 로컬 및 리모트에서 삭제합니다.
5. develop 브랜치가 배포가능한 상태가 되었을때, master 로 merge 합니다.

---

***핵심***

- 모든 작업은 반드시 특정 브랜치에서 작업 후, `pull request` 를 통해 다른 개발자들의 코드 리뷰를 받고 `merge`를 해야 함
- 완료된 브랜치는 삭제되어야 함
- 충돌이 발생하지 않도록, `브랜치 병합 전에는 항상 pull` 명령어로 최신 코드를 가져와야함
- 코드 변경사항이 작을수록 충돌 가능성이 낮아지기 때문에, 작은 기능 단위로 브랜치를 만들고 작업

---

<aside>
💡 commit 에 관련된 convention 은 [Git Commit Convention](https://www.notion.so/Git-Commit-Convention-ff0420d79bae4a3192e150820742e8f4) 을 참조해주세요.

</aside>

```java
1. 기능개발시 - feat/fe||be/"개발 기능 내용"
2. 버그 수정시 - bug/fe||be/"수정하는 버그 내용"
3. hotfix 시 - hotfix/fe||be/"내용"
4. 리팩토링 시 - refactor/fe||be/"내용"
5. 문서 작업 시 - docs/fe||be/"내용"
6. 파일 구조 변경 시 - chore/fe||be/"내용"
7. 테스트 작성 시 - test/fe||be/"내용"

🔥 브랜치 이름은 기본적으로 소문자 영어와 '/', '-' 만 사용해주세요.
🔥 대분류와 기능 사이에 BackEnd 의 be 혹은 FrontEnd 의 fe 를 작성해주세요.
🔥 위 예시에 포함되지 않는 작업을 수행해야 할때에는 배창민을 호출해주세요.
```

# 3. References

[Git 브랜칭 전략 : Git-flow와 Github-flow](https://hellowoori.tistory.com/56)