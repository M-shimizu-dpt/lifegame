# 初めに
このアプリは桃鉄を模したゲームです。<br>
クラスはmain,object,eventにそれぞれ入っています。<br>
※javaの勉強のために作成したアプリなので、必ずしも効率的な処理をしているわけではありません。<br>

# main
mainにはApp.javaが入っています。<br>
App.javaではアプリのmainクラスとしてゲームのターン管理などを行っています。

# object
objectにはアプリで管理すべきオブジェクトが入っています。<br>
中には、map、ボンビー情報を管理するBinbo.java、プレイヤー情報を管理するPlayer.java、そのプレイヤーに付いているバフを管理するBuff.java、カード情報を管理するCard.java、サイコロ情報を管理するDice.javaが入っています。<br>
mapにはprintとinformationの二つが入っています。<br>
printには画面表示を管理するWindow.javaが入っています。<br>
informationにはマップ全体の情報を管理するJapan.java、駅情報を管理するStation.java、物件情報を管理するProperty.java、位置座標を表すCoordinates.javaが入っています。<br>

# event
eventにはsearch、決算処理をするClosingEvent.java、マスに止まった時の処理をするMassEvent.java、マスを移動する時の処理をするMoveEvent.java、ランダムイベント処理をするRandomEvent.java、各処理が終わるまでメインスレッドを待たせる処理をするWaitThread.javaが入っています。<br>
searchには現在の移動可能数で行ける全てのマスを探索するMassSearchThread.java、行くことが出来るマスの内目的地に最も近いマスを探索するNearestSearchThread.java、目的地までの最短距離のみを取得するOnlyDistanceSearchThread.java、目的地までの最短経路を探索するSearchThread.java、最寄りの店を探索するShopSearchThread.java、最寄り駅を探索するStationSearchThread.java、これらすべての親クラスであるmodel/SearchThreadModel.java、これらのThreadを使って探索させるSearcherが入っています。<br>

# システムの流れ
ユーザがactionを起こすと、それを実現するEventが発生します。<br>
EventはそれぞれにあったEventが処理を行います。<br>
これによって、各クラスは他のクラスを意識することなく実装することが出来、可読性の向上が見込めます。<br>
