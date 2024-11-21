<!-- textlint-disable @textlint-rule/require-header-id -->
<!-- markdownlint-disable-file CMD001 -->

# Contributing to AlesInfiny Maia OSS Edition

AlesInfiny Maia OSS Edition へのコントリビュートを検討いただきありがとうございます。

AlesInfiny Maia OSS Edition オープンソースプロジェクト（以下、本プロジェクト）において、様々な種類のコントリビュートがどのように処理されるか説明します。
コントリビュートの前に、関連するセクションをお読みください。
これにより、メンテナーの作業が簡単になり、素早くコントリビュートが取り込まれるようになります。
本プロジェクトは、あなたのコントリビュートを楽しみにしています。

本プロジェクトにコントリビュートする時間がない場合、以下の様々な方法で支援いただけると大変助かります。

- GitHub プロジェクト、 GitHub リポジトリにスターを付ける
- 本プロジェクトについて SNS やブログで発信する
- あなたの所属する組織に、本プロジェクトの存在を知らせる

## 本プロジェクトに対する質問

質問をする前に、役に立ちそうな既存の Issue やプルリクエスト、 Discussions がないか確認してください。
Issue や Discussions に対して追加の説明や質問がある場合は、既存のものに書き込んでください。
一般的な技術に関する質問は、インターネットで検索することも有用です。

これらでは解決できない場合、 Discussions の [Q&A](https://github.com/AlesInfiny/maia/discussions/categories/05-q-a) より質問をお寄せください。
質問を投稿する場合、直面していることについてできる限り多くの情報を提供してください。
可能な限りスピーディーに対応いたします。
ただし、質問に対する回答が常に得られることを期待しないでください。

## 本プロジェクトに対するコントリビュート

### 法律上の留意事項

本プロジェクトに対してコントリビュートする場合、コントリビュートしたコンテンツが、本プロジェクトのライセンスに基づいて公開・提供されることに同意する必要があります。
またコントリビュートするコンテンツが、すべてあなたがオリジナルのコンテンツであり、必要な権利を持っていることを保証しなければなりません。

### 不具合の報告

#### 不具合の報告をする前に

良い不具合の報告には、できる限り正確で詳細な、事実に基づく情報を含めます。
メンテナーが、不具合を再現するために必要な詳細情報の提供にご協力ください。
また不具合を見つけた場合、事前に以下を確認してください。

- 最新バージョンを利用しているか確認してください。
- 本プロジェクトの前提とする環境以外で利用されていないか確認してください。
- 既に他のユーザーやメンテナーによって、同じ問題が報告されていないか、 Issue や Discussions の情報を確認してください。
- 不具合に対して、既に解決に向けた作業が開始していないか、プルリクエストを確認してください。
- 不具合に関する詳細な情報を収集できているか確認してください。
    - スタックトレースが存在しますか？
    - 不具合の発生する OS 、プラットフォーム、バージョンは明らかですか？
    - 不具合を引き起こす入力および不具合が発生したときの出力値は明らかですか？
    - 再現性の有無は明らかですか？
    - 再現手順は確立していますか？

#### 良い不具合の報告方法

報告したい不具合が機密情報を含むセキュリティ関連の問題、脆弱性である場合、 Issue や Discussions 、プルリクエストなど公共の場を **利用しないで** ください。
機密性の高い不具合は、 [こちら](https://github.com/AlesInfiny/maia/security/advisories/new) から報告するようお願いします。

その他一般的な不具合は、 Issue を用いて報告してください。
報告には「不具合の報告」のテンプレートを用いてください。
期待する動作と実際の動作を説明し、再現手順、環境を明らかにしてください。
問題の発生する最小の構成物があれば、是非提供をお願いします。
また可能であれば解決策の提案を含めてください。

不具合の Issue 提出後、内容に応じてメンテナーはラベル付けを行います。
報告者はラベルを付与しないでください。
メンテナーは提出された再現手順、環境の情報をもとに、不具合の再現を試みます。
不具合が再現された場合、その Issue はメンテナーによって適切に処理されます。
報告者からのプルリクエストもお待ちしています。

### 機能やドキュメント拡張の提案

全く新しい機能やドキュメント、既存機能や既存ドキュメントに対する改善など、機能強化の提案を提出する手順を説明します。

#### 提案を送信する前に

- 最新バージョンを使用していることを確認してください。
- Issue 、プルリクエスト、 Discussions を検索して、既に同様の提案がされているか確認してください。
  類似するものが見つかった場合は、新たに Issue を登録せず、既存のものにコメントを追加してください。
- アイデアが多くのユーザーに役立つものであるか確認してください。
  対象ユーザーが少ない場合、提案の採用されない可能性が高まります。
- 提案がプロジェクトの範囲や目的に合っているか確認してください。
  また素晴らしい提案であっても、プロジェクトの保守コストが増加してしまうものは、採用されない可能性が高まります。

#### 優れた提案の提出方法

新しい機能およびドキュメント、既存機能や既存ドキュメントに対する改善は、 Issue を用いて提案してください。
提案には「機能要求」のテンプレートを用いて、以下の情報を含めるようにしてください。

- 提案を識別するために、 Issue には明確で説明的なタイトルを付けてください。
- 現在の機能やドキュメントについて説明したうえで、どのように改善するべきか詳細に記述してください。
- 改善によって発生することが予想される良い影響、悪い影響があれば記述してください。
- 必要に応じて、現状の機能やドキュメントの状態を示すスクリーンショットや、アニメーションを含めてください。
- 機能やドキュメントの追加および修正によって、どのような人がどの程度その恩恵を受けるか説明してください。

Issue 提出後、内容に応じてメンテナーはラベル付けを行います。
報告者はラベルを付与しないでください。

提出された機能要求に関して Issue および Discussions を通じてメンテナーから連絡することがあります。

### コード / ドキュメントのコントリビュート

コードの修正および作成を含むコントリビュートを送信する前に、コードのビルドが成功することと、既存のテストにすべて合格することを確認してください。
テストには、動的テスト、静的テストがあり、そのすべてに合格しなければなりません。
また、修正および作成したコードに対する自動テストを追加してください。

ドキュメントの修正および作成を含むコントリビュートを送信する前に、各種 Lint ツールによる警告が発生しないことと、ドキュメントのビルドが成功することを確認してください。
詳細は [こちら](/documents/README.md#ドキュメント作成手順) を参照してください。

コントリビュートの提出には、以下の手順を踏んでください。

1. このリポジトリの独自のフォークを作成します。
1. フォークしたリポジトリに対してあなたの変更を反映します。
1. このリポジトリに対してフォークしたリポジトリからプルリクエストを送信してください。
   プルリクエストには、テンプレートに従って情報を記載してください。

## レビュープロセス

コントリビュートは本プロジェクトのメンテナーによってレビューされます。
メンテナーからのレビューコメントを受領した場合は、修正またはコメントに対するレスポンスをお願いいたします。
メンテナーからの連絡に対して 2 週間以上レスポンスがない場合、 Issue やプルリクエストをクローズすることがあります。

## スタイルガイド

本プロジェクトでは、静的テストによりコードおよびドキュメントのスタイルを統一します。
コンパイラーや各種 Lint ツールによる警告が発生しないようにコードとドキュメントを作成してください。
原則として警告は抑制しないでください。

### .NET アプリケーション、Vue.js アプリケーションの規約

[AlesInfiny Maia OSS Edition のコーディング規約](/documents/contents/guidebooks/conventions/coding-conventions.md) をご覧ください。

### ドキュメントの規約

mkdocs でビルドするドキュメントは、 [この規約](/documents/README.md#ドキュメント作成手順) に従って作成してください。

その他のドキュメントは、以下のルールを無効にし、 Lint ツールを実行してください。

- textlint : @textlint-rule/require-header-id
- markdownlint : CMD001

Lint ツールは、リポジトリのルートディレクトリで、以下のコマンドを利用して実行します。

```bat
npm run lint
```

## 行動規範

[行動規範](/.github/CODE_OF_CONDUCT.md) をご覧ください。

<!-- textlint-enabled @textlint-rule/require-header-id -->