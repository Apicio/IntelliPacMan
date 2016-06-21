package pacman;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Random;
import AbPruning.MinMaxPacman;
import AbPruning.MinMaxPacman2;
import DecisionTree.Greedy;
import Deep.MyAStar;
import Deep.MyRandomPacMan2;
import pacman.controllers.Controller;
import pacman.controllers.HumanAggressiveGhosts;
import pacman.controllers.HumanController;
import pacman.controllers.KeyBoardInput;
import pacman.controllers.examples.AggressiveGhosts;
import pacman.controllers.examples.Legacy;
import pacman.controllers.examples.Legacy2TheReckoning;
import pacman.controllers.examples.NearestPillPacMan;
import pacman.controllers.examples.NearestPillPacManVS;
import pacman.controllers.examples.RandomGhosts;
import pacman.controllers.examples.RandomNonRevPacMan;
import pacman.controllers.examples.RandomPacMan;
import pacman.controllers.examples.StarterGhosts;
import pacman.controllers.examples.StarterPacMan;
import pacman.entries.pacman.Ambush;
import pacman.game.Game;
import pacman.game.GameView;
import pacman.game.Constants.MOVE;

import static pacman.game.Constants.*;

/**
 * This class may be used to execute the game in timed or un-timed modes, with or without
 * visuals. Competitors should implement their controllers in game.entries.ghosts and 
 * game.entries.pacman respectively. The skeleton classes are already provided. The package
 * structure should not be changed (although you may create sub-packages in these packages).
 */
@SuppressWarnings("unused")
public class Executor
{	
	/**
	 * The main method. Several options are listed - simply remove comments to use the option you want.
	 *
	 * @param args the command line arguments
	 */
	private static int numTrials=10;
	private static int delay=1;
	private static boolean visual=true;
	private static Executor exec=new Executor();
	private static HashMap<String, Integer> bestScores;

	public static void main(String[] args)
	{
		
		try {
		/* Load Best Scores*/
		File f = new File("Scores.ser");
		if(f.exists()){
			FileInputStream fout = new FileInputStream("Scores.ser");
			ObjectInputStream ois = new ObjectInputStream(fout);
			bestScores = (HashMap<String, Integer>) ois.readObject(); 
		}else{
			bestScores = new HashMap<String, Integer>();
		}
		Controller<MOVE> pacmanController;
		Controller<EnumMap<GHOST,MOVE>> ghostController;
		ghostController = new AggressiveGhosts();
//		pacmanController = new MyAStar(ghostController);
//		pacmanController = new MyRandomPacMan(new AggressiveGhosts());
//		pacmanController = new Greedy();
		pacmanController = new MinMaxPacman(new AggressiveGhosts(),120,false);
		
		
		
		/* Tests Running */
		exec.runExperiment(pacmanController,ghostController,5,true,true);		
		/* Save Scores */	
		FileOutputStream fout = new FileOutputStream("Scores.ser");
		ObjectOutputStream oos = new ObjectOutputStream(fout);
		oos.writeObject(bestScores);
		
		/* Replay Best */
		exec.replayGame(pacmanController.getClass().getName()+""+ghostController.getClass().getName(),visual);
		
		
		
		//exec.runGameTurn(new StarterPacMan(),new StarterGhosts(),visual,true, delay);

		//run multiple games in batch mode - good for testing.
		//		boolean canReverse = true;
		//		for(int i=2; i<220; i++){	
		//			//exec.runExperiment(new MinMaxPacman(new AggressiveGhosts(),i,canReverse),new AggressiveGhosts(),numTrials); 
		//			exec.runExperiment(new MinMaxPacman(new AggressiveGhosts(),i,!canReverse),new AggressiveGhosts(),numTrials); 
		//			System.out.println("DEEP = "+i);
		//		}
 
		//		int numTrials=10;
		//		exec.runExperiment(new MyRandomPacMan2(new AggressiveGhosts()),new AggressiveGhosts(),numTrials); 
		// 
		//		
		//		
		//		exec.runGame(new MyAStar(new AggressiveGhosts()),new AggressiveGhosts(),visual,delay);
		//run a game in synchronous mode: game waits until controllers respond.
		//		int delay=10;
		//		boolean visual=true;
		//		exec.runGame(new MyAstar(new RandomGhosts()),new RandomGhosts(),visual,delay);
		//        System.out.println("Trial: 1 Generations: 100 PopSize: 50");
		//        CambrianExplosion boom1 = new CambrianExplosion(100, 50, 1, new MinMaxPacman(new AggressiveGhosts(),30,false),new AggressiveGhosts());
		//        boom1.explode();
		//        System.out.println("Trial: 2 Generations: 5 PopSize: 20");
		//        CambrianExplosion boom2 = new CambrianExplosion(5, 20, 1, new MinMaxPacman(new AggressiveGhosts(),30,false),new AggressiveGhosts());
		//        boom2.explode();
		//        System.out.println("Trial: 3 Generations: 10 PopSize: 10");
		//        CambrianExplosion boom3 = new CambrianExplosion(10, 10, 1, new MinMaxPacman(new AggressiveGhosts(),30,false),new AggressiveGhosts());
		//        boom3.explode();
		//        System.out.println("Trial: 4 Generations: 10 PopSize: 20");
		//        CambrianExplosion boom4 = new CambrianExplosion(10, 20, 1, new MinMaxPacman(new AggressiveGhosts(),30,false),new AggressiveGhosts());
		//        boom4.explode();
		//        System.out.println("Trial: 5 Generations: 20 PopSize: 20");
		//        CambrianExplosion boom5 = new CambrianExplosion(20, 20, 1, new MinMaxPacman(new AggressiveGhosts(),30,false),new AggressiveGhosts());
		//        boom5.explode();
		//        System.out.println("Trial: 6 Generations: 100 PopSize: 20");
		//        CambrianExplosion boom6 = new CambrianExplosion(100, 20, 1, new MinMaxPacman(new AggressiveGhosts(),30,false),new AggressiveGhosts());
		//        boom6.explode();
		//
		//		//exec.runGame(new MinMaxPacman(new StarterGhosts(),5,false),new StarterGhosts(),visual,delay);
		//		

		///*
		//run the game in asynchronous mode.
		//		boolean visual=true;
		//		exec.runGameTimed(new NearestPillPacMan(),new AggressiveGhosts(),visual);
		//		exec.runGameTimed(new StarterPacMan(),new StarterGhosts(),visual);
		//		exec.runGameTimed(new HumanController(new KeyBoardInput()),new AggressiveGhosts(),visual);	
		//		exec.runGameTimed(new DecisionTree(),new AggressiveGhosts(),visual);	
		//*/

		/*
		//run the game in asynchronous mode but advance as soon as both controllers are ready  - this is the mode of the competition.
		//time limit of DELAY ms still applies.
		boolean visual=true;
		boolean fixedTime=false;
		exec.runGameTimedSpeedOptimised(new RandomPacMan(),new RandomGhosts(),fixedTime,visual);
		 */

		
		//run game in asynchronous mode and record it to file for replay at a later stage.
/*		boolean visual=true;
		String fileName="replay.txt";
		exec.runGameTimedRecorded(new HumanController(new KeyBoardInput()),new RandomGhosts(),visual,fileName);
		//exec.replayGame(fileName,visual);
		 */
		} catch (IOException | ClassNotFoundException e) {e.printStackTrace();}
	}

	private int bestScore;

	/**
	 * For running multiple games without visuals. This is useful to get a good idea of how well a controller plays
	 * against a chosen opponent: the random nature of the game means that performance can vary from game to game. 
	 * Running many games and looking at the average score (and standard deviation/error) helps to get a better
	 * idea of how well the controller is likely to do in the competition.
	 *
	 * @param pacManController The Pac-Man controller
	 * @param ghostController The Ghosts controller
	 * @param trials The number of trials to be executed
	 */
	public void runExperiment(Controller<MOVE> pacManController,Controller<EnumMap<GHOST,MOVE>> ghostController,int trials, boolean visual, boolean doReplay)
	{
		double avgScore=0;	
		String fileName = pacManController.getClass().getName()+""+ghostController.getClass().getName();
		
		Random rnd=new Random(0);
		Game game;
		GameView gv=null;
		System.out.print("<> ");
		for(int i=0;i<trials;i++)
		{
			StringBuilder replay=new StringBuilder();
			int currBestScore = 0;
			if(bestScores.get(fileName) != null)
				currBestScore = bestScores.get(fileName);
			game=new Game(rnd.nextLong());
			
			if(visual)
				if(gv == null)
					gv=new GameView(game).showGame();
				else
					gv.updateGame(game);

			while(!game.gameOver())
			{
				game.advanceGame(pacManController.getMove(game.copy(),System.currentTimeMillis()+DELAY),
						ghostController.getMove(game.copy(),System.currentTimeMillis()+DELAY));

				if(visual)
					gv.repaint();
				
				if(doReplay)
					replay.append(game.getGameState()+"\n");
			}
			avgScore+=game.getScore();
			System.out.print(game.getScore()+" <> ");
			
			if(game.getScore()>currBestScore){
				saveToFile(replay.toString(),fileName,false);
				bestScores.put(fileName, game.getScore());
			}
			System.out.println("BestScore:" +bestScores.get(fileName));
		}
		double _mean = avgScore/trials;
		System.out.println("\n"+_mean);
		bestScores.put(fileName+"MEAN", (int) _mean);
	}
	public double runExperimentGene(Controller<MOVE> pacManController,Controller<EnumMap<GHOST,MOVE>> ghostController,int trials)
	{
		double avgScore=0;

		Random rnd=new Random(0);
		Game game;

		System.out.print("<> ");
		for(int i=0;i<trials;i++)
		{
			game=new Game(rnd.nextLong());

			while(!game.gameOver())
			{
				game.advanceGame(pacManController.getMove(game.copy(),System.currentTimeMillis()+DELAY),
						ghostController.getMove(game.copy(),System.currentTimeMillis()+DELAY));


			}

			avgScore+=game.getScore();
			System.out.print(game.getScore()+" <> ");
		}

		return (avgScore/trials);
	}

	/**
	 * Run a game in asynchronous mode: the game waits until a move is returned. In order to slow thing down in case
	 * the controllers return very quickly, a time limit can be used. If fasted gameplay is required, this delay
	 * should be put as 0.
	 *
	 * @param pacManController The Pac-Man controller
	 * @param ghostController The Ghosts controller
	 * @param visual Indicates whether or not to use visuals
	 * @param delay The delay between time-steps
	 */
	public void runGame(Controller<MOVE> pacManController,Controller<EnumMap<GHOST,MOVE>> ghostController,boolean visual,int delay)
	{
		Game game=new Game(0);

		GameView gv=null;

		if(visual)
			gv=new GameView(game).showGame();

		while(!game.gameOver())
		{
			game.advanceGame(pacManController.getMove(game.copy(),-1),ghostController.getMove(game.copy(),-1));

			try{Thread.sleep(delay);}catch(Exception e){}

			if(visual)
				gv.repaint();
		}
	}
	public void runGameTurn(Controller<MOVE> pacManController,Controller<EnumMap<GHOST,MOVE>> ghostController,boolean visual,boolean isPacman,int delay)
	{
		try {
			Game game=new Game(0);

			GameView gv=null;

			if(visual)
				gv=new GameView(game).showGame();

			while(!game.gameOver())
			{
				if(isPacman)
					game.advancePacMan(pacManController.getMove(game.copy(),-1));
				else
					game.advanceGhosts(ghostController.getMove(game.copy(),-1));
				Thread.sleep(delay);
				if(visual)
					gv.repaint();
			}
		} catch (InterruptedException e1) {e1.printStackTrace();}
	}

	public void runGamePacMan(Controller<MOVE> pacManController,boolean visual,int delay)
	{
		Game game=new Game(0);

		GameView gv=null;

		if(visual)
			gv=new GameView(game).showGame();

		while(!game.gameOver())
		{


			try{Thread.sleep(delay);}catch(Exception e){}

			if(visual)
				gv.repaint();
		}
	}

	/**
	 * Run the game with time limit (asynchronous mode). This is how it will be done in the competition. 
	 * Can be played with and without visual display of game states.
	 *
	 * @param pacManController The Pac-Man controller
	 * @param ghostController The Ghosts controller
	 * @param visual Indicates whether or not to use visuals
	 */
	public void runGameTimed(Controller<MOVE> pacManController,Controller<EnumMap<GHOST,MOVE>> ghostController,boolean visual)
	{
		Game game=new Game(0);

		GameView gv=null;

		if(visual)
			gv=new GameView(game).showGame();

		if(pacManController instanceof HumanController)
			gv.getFrame().addKeyListener(((HumanController)pacManController).getKeyboardInput());

		new Thread(pacManController).start();
		new Thread(ghostController).start();

		while(!game.gameOver())
		{
			pacManController.update(game.copy(),System.currentTimeMillis()+DELAY);
			ghostController.update(game.copy(),System.currentTimeMillis()+DELAY);

			try
			{
				Thread.sleep(DELAY);
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}

			game.advanceGame(pacManController.getMove(),ghostController.getMove());	   

			if(visual)
				gv.repaint();
		}

		pacManController.terminate();
		ghostController.terminate();
	}

	/**
	 * Run the game in asynchronous mode but proceed as soon as both controllers replied. The time limit still applies so 
	 * so the game will proceed after 40ms regardless of whether the controllers managed to calculate a turn.
	 *     
	 * @param pacManController The Pac-Man controller
	 * @param ghostController The Ghosts controller
	 * @param fixedTime Whether or not to wait until 40ms are up even if both controllers already responded
	 * @param visual Indicates whether or not to use visuals
	 */
	public void runGameTimedSpeedOptimised(Controller<MOVE> pacManController,Controller<EnumMap<GHOST,MOVE>> ghostController,boolean fixedTime,boolean visual)
	{
		Game game=new Game(0);

		GameView gv=null;

		if(visual)
			gv=new GameView(game).showGame();

		if(pacManController instanceof HumanController)
			gv.getFrame().addKeyListener(((HumanController)pacManController).getKeyboardInput());

		new Thread(pacManController).start();
		new Thread(ghostController).start();

		while(!game.gameOver())
		{
			pacManController.update(game.copy(),System.currentTimeMillis()+DELAY);
			ghostController.update(game.copy(),System.currentTimeMillis()+DELAY);

			try
			{
				int waited=DELAY/INTERVAL_WAIT;

				for(int j=0;j<DELAY/INTERVAL_WAIT;j++)
				{
					Thread.sleep(INTERVAL_WAIT);

					if(pacManController.hasComputed() && ghostController.hasComputed())
					{
						waited=j;
						break;
					}
				}

				if(fixedTime)
					Thread.sleep(((DELAY/INTERVAL_WAIT)-waited)*INTERVAL_WAIT);

				game.advanceGame(pacManController.getMove(),ghostController.getMove());	
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}

			if(visual)
				gv.repaint();
		}

		pacManController.terminate();
		ghostController.terminate();
	}

	/**
	 * Run a game in asynchronous mode and recorded.
	 *
	 * @param pacManController The Pac-Man controller
	 * @param ghostController The Ghosts controller
	 * @param visual Whether to run the game with visuals
	 * @param fileName The file name of the file that saves the replay
	 */
	public void runGameTimedRecorded(Controller<MOVE> pacManController,Controller<EnumMap<GHOST,MOVE>> ghostController,boolean visual,String fileName)
	{
		StringBuilder replay=new StringBuilder();

		Game game=new Game(0);

		GameView gv=null;

		if(visual)
		{
			gv=new GameView(game).showGame();

			if(pacManController instanceof HumanController)
				gv.getFrame().addKeyListener(((HumanController)pacManController).getKeyboardInput());
		}		

		new Thread(pacManController).start();
		new Thread(ghostController).start();

		while(!game.gameOver())
		{
			pacManController.update(game.copy(),System.currentTimeMillis()+DELAY);
			ghostController.update(game.copy(),System.currentTimeMillis()+DELAY);

			try
			{
				Thread.sleep(DELAY);
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}

			game.advanceGame(pacManController.getMove(),ghostController.getMove());	        

			if(visual)
				gv.repaint();

			replay.append(game.getGameState()+"\n");
		}

		pacManController.terminate();
		ghostController.terminate();

		saveToFile(replay.toString(),fileName,false);
	}

	/**
	 * Replay a previously saved game.
	 *
	 * @param fileName The file name of the game to be played
	 * @param visual Indicates whether or not to use visuals
	 */
	public void replayGame(String fileName,boolean visual)
	{
		PrintStream orig = System.err;
		System.setErr(new PrintStream(new OutputStream() {
		    @Override public void write(int b) throws IOException {}
		}));
		
		ArrayList<String> timeSteps=loadReplay(fileName);

		Game game=new Game(0);

		GameView gv=null;

		if(visual)
			gv=new GameView(game).showGame();

		for(int j=0;j<timeSteps.size();j++)
		{			
			game.setGameState(timeSteps.get(j));

			try
			{
				Thread.sleep(2);
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
			if(visual)
				gv.repaint();
		}
		System.setErr(orig);
	}

	//save file for replays
	public static void saveToFile(String data,String name,boolean append)
	{
		try 
		{
			FileOutputStream outS=new FileOutputStream(name,append);
			PrintWriter pw=new PrintWriter(outS);

			pw.println(data);
			pw.flush();
			outS.close();

		} 
		catch (IOException e)
		{
			System.out.println("Could not save data!");	
		}
	}  

	//load a replay
	private static ArrayList<String> loadReplay(String fileName)
	{
		ArrayList<String> replay=new ArrayList<String>();

		try
		{         	
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));	 
			String input=br.readLine();		

			while(input!=null)
			{
				if(!input.equals(""))
					replay.add(input);

				input=br.readLine();	
			}
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}

		return replay;
	}
}