# swing-chess
A game of chess created with the Java Swing library.

==========================================================

To Play:

Stockfish Chess engine is needed for true AI implementation. To install Stockfish please visit: https://stockfishchess.org/download/. Put the path to the engine in class StockFishAI.java on line 26: stockfishProcess = new ProcessBuilder("/Path to engine/").start(); If you have a Mac and use homebrew you should be able to leave it as is.

SimpleAI in Engine run() can be used to generate AI moves if you don't want to download and install Stockfish, just comment out stockFishAI() and un-comment out simpleAI(). However, keep in mind that simpleAI is exactly that; simple. It generates a move by obtaining all available moves for the AI player and picking one at random. Please feel free to try and develop your own generateSimpleAiMove algorithm if that's something you'd be interested in doing. 

==========================================================

Credits:

Special credit goes to Andrei Ciobanu and his article "Writing a Universal Chess Interface (UCI) Client in Java" at https://www.andreinc.net/2021/04/22/writing-a-universal-chess-interface-client-in-java for providing insights on how UCI works. His generic method command() was used to implement class StockFishAI.

==========================================================

To-do's:

1) Gameplay 
   1) Implement the 'En passant' pawn jump ability.
   2) Differentiate between a Rook's normal move and its Castle move
   3) Do not allow King to Castle out of check
   
2) UI
   1) Add Begin Game menu
      1) Select Human or AI
      2) Select Team 1 or 2
      3) Select Piece skins
   2) Add Settings UI
      1) AI Difficulty Selection

Stockfish is insanely strong. Currently in the process of working out appropriate 'handicaps' to bring down the strength of Stockfish's gameplay. You can do this manually by decreasing its 'thinking' time and depth of search in the AiDifficulty class when it is initialized in class Engine.
