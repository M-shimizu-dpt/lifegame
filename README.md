# 初めに
> このアプリはjavaで書かれた桃鉄を模したゲームです。<br>
> クラスはmain,object,eventにそれぞれ入っています。<br>
> ※javaの勉強のために作成したアプリなので、必ずしも効率的な処理をしているわけではありません。<br>

# main
> * App.java　→　アプリのmainクラスとしてゲームのターン等を管理する

# object
> objectにはアプリで管理すべきオブジェクトが入っています。<br>
> * model
>> * CardModel.java →　Card.javaの親クラスでCardが出来る基本機能を持っている
> * map
>> * print
>>> * diceImage
>>>> * サイコロ画面で表示する予定のサイコロの写真が入っている
>>> * animation
>>>> * 今後実装予定のアニメーション処理をする為のThreadを格納する
>>> * frames
>> * information
> * Binbo.java　→　ボンビー情報を管理する
> * Player.java　→　プレイヤー情報を管理する
> * Buff.java　→　そのプレイヤーに付いているバフを管理する
> * Card.java　→　カード情報を管理する
> * Dice.java　→　サイコロ情報を管理する


#### frames
> * card
> * closing
> * map
> * model
> * property
> * BinboFrame.java → ボンビーイベントの結果表示用フレーム
> * ConfirmationFrame.java → ポップアップ表示用フレーム
> * DiceFrame.java → サイコロ画面表示用フレーム
> * GoalFrame.java → ゴール画面表示用フレーム
> * InfoFrame.java → 会社情報表示用フレーム
> * RandomFrame.java → randomイベントの結果表示用フレーム
> * StartFrame.java → 初期画面表示用フレーム
##### card
### information
> * Japan.java　→　マップ全体の情報を管理する
> * Station.java　→　駅情報を管理する
> * Property.java　→　物件情報を管理する
> * Coordinates.java　→　位置座標を表す

# event
> * search
> * ClosingEvent.java　→　決算処理をする
> * ContainsEvent.java →　各クラスの値を比較する
> * MassEvent.java　→　マスに止まった時の処理をする
> * MoveEvent.java　→　マスを移動する時の処理をする
> * RandomEvent.java　→　ランダムイベント処理をする
> * SaleEvent.java →　購入・売却処理をする
> * Searcher.java　→　これらのThreadを使って探索させる
> * WaitThread.java　→　各処理が終わるまでメインスレッドを待たせる処理をする
> * FrameEvent.java　→　画面表示を管理する
## search
> * model
> * MassSearchThread.java　→　現在の移動可能数で行ける全てのマスを探索する
> * NearestSearchThread.java　→　行くことが出来るマスの内目的地に最も近いマスを探索する
> * OnlyDistanceSearchThread.java　→　目的地までの最短距離のみを取得する
> * SearchThread.java　→　目的地までの最短経路を探索する
> * ShopSearchThread.java　→　最寄りの店を探索する
> * StationSearchThread.java　→　最寄り駅を探索する
### model
> * SearchThreadModel.java　→　search以下のThreadすべての親クラスでSearchThreadが出来る基本機能を持っている

# システムの流れ
> ユーザがactionを起こすと、それを実現するEventが発生します。<br>
> EventはそれぞれにあったEventが処理を行います。<br>
> これによって、各クラスは他のクラスを意識することなく実装することが出来、可読性の向上が見込めます。<br>

## 例1)ゲームの初期設定をする
> 1) Appにユーザが入力した情報が入る
> 2) この情報をEventに送信
> 3) Eventが入力情報を基に各オブジェクトの情報を書き変える

## 例2)カード情報を表示する
> 1) ユーザがカードボタンを押す
> 2) WindowがEventにプレイヤーが持つカード一覧の取得を依頼
> 3) EventがPlayerのCard情報を取得しWindowに渡す
> 4) Windowがその情報を基に画面に表示
