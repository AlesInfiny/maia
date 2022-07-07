# プロジェクトの雛型作成

## Spring Initializr の利用 ## {: #use-spring-initializr }

各gradleプロジェクトの雛型は、Spring Initializr を利用して作成します。
Spring Initializr は Spring Boot を利用するプロジェクトの雛型を簡潔に作成することができるツールです。
[webサービス](https://start.spring.io/)を利用する方法や、
各種 IDE にプラグインや拡張機能を導入することで IDE 上で利用することもできます。
VS Code の場合、[Spring Boot Extension Pack](https://marketplace.visualstudio.com/items?itemName=Pivotal.vscode-boot-dev-pack) が導入済みであれば利用することが可能です。

ここではwebサービスでの作成を前提に説明します。

## ルートプロジェクトの作成 ## {: #create-root-project }

ルートプロジェクトの雛型をSpring Initializr を利用して作成します。
web画面上の各種設定項目は以下の通りです。

- Project

  プロジェクトのビルドツールを Gradle と Apache Maven から選択します。
  Maia OSS 版では Gradle をビルドツールとして選択することをデフォルトとしており、以降の説明でも Gradle でビルドすることを前提として解説していますが、Apache Maven を選択することも可能です。

- Language

  開発言語として Java を選択します。

- Spring Boot
  
  利用する Spring Boot のバージョンを選択します。
  ベースとなる JDK や Spring Framework のバージョンを考慮して選択します。特に考慮点がなければ最新の GA 版を選択することを推奨します。
  選択できる Spring Boot のバージョンは、[OSS サポート期限内のバージョン](https://spring.io/projects/spring-boot#support)に限られ、古いバージョンは指定できません。

- Project Meta Data：Artifact

  プロジェクトの基点のフォルダ名となるプロジェクト名を設定します。

- Project Meta Data：その他

  ルートプロジェクトの雛型作成では設定不要（任意）です。

- Dependencies

  各サブプロジェクト全体で利用するパッケージがあれば登録します。
  後続手順の[プロジェクトの共通設定](./common-project-settings.md)にて、後から追加することもできます。
  登録すべき依存ライブラリについては、[プロジェクトの共通設定](./common-project-settings.md)を参照してください。

## サブプロジェクトの作成 ## {: #create-sub-projects }

ルートプロジェクトと同様に、サブプロジェクトの雛型をSpring Initializr を利用して作成します。
以下、サブプロジェクト毎に異なる設定項目について説明します。その他の項目はルートプロジェクトと同様に設定してください。

- Project Meta Data：Group

  対象プロジェクトが含まれるグループ名を設定します。
  対象プロジェクトのパッケージ名は、このグループ名から始まることが想定されます。
  ハイフンなど、パッケージ名に利用不可の文字を指定した場合、パッケージ名としては取り除かれますが、元々使わないことが望ましいです。

- Project Meta Data：Artifact

  サブプロジェクトの基点のフォルダ名となるプロジェクト名を設定します。
  また、グループ名に続くパッケージ名としても利用されますので、グループ名と同様にパッケージ名の命名規則に合った名前にすることが望ましいです。
  
- Project Meta Data：Name

  アプリケーション名を設定します。
  この名前はSpring Bootの実行クラス(Main クラス)のクラス名やパッケージした際のwarやjar名に利用されます。

- Dependencies

  プロジェクトで利用するパッケージがあれば設定します。
  後続手順の[サブプロジェクトの個別設定](./subproject-settings.md)にて、後から追加することもできます。
  各プロジェクトの依存ライブラリについては、[サブプロジェクトの個別設定](./subproject-settings.md)を参照してください。

## ルートプロジェクトとサブプロジェクトの配置 ## {: #put-projects }

ルートプロジェクトと各サブプロジェクトの雛型を作成したら、
これらを展開し適切に配置します。

各雛型は zip 形式で生成されるため、まずは全ての雛型を展開します。
その後、ルートプロジェクトのフォルダ直下に各サブプロジェクトのフォルダを配置します。
