# 初めに
このアプリは桃鉄を模したゲームです。<br>
クラスはmain,object,eventにそれぞれ入っています。<br>
※javaの勉強のために作成したアプリなので、必ずしも効率的な処理をしているわけではありません。<br>

# main
mainにはApp.javaが入っています。<br>
App.javaではアプリのmainクラスとしてゲームのターン管理などを行っています。

# object
objectにはアプリで管理すべきオブジェクトが入っています。<br>
mapにはprintとinformationの二つが入っています。<br>
printには画面表示を管理するWindow.javaが入っています。<br>
informationにはマップ全体の情報を管理するJapan.java、駅情報を管理するStation.java、物件情報を管理するProperty.java、位置座標を表すCoordinates.javaが入っています。<br>

中には、ボンビー情報を管理するBinbo.java、プレイヤー情報を管理するPlayer.java、そのプレイヤーに付いているバフを管理するBuff.java、カード情報を管理するCard.java、サイコロ情報を管理するDice.javaが入っています。<br>

# event
eventには
