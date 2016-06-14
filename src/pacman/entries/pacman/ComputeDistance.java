package pacman.entries.pacman;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.EnumMap;
import java.util.HashMap;

import pacman.controllers.Controller;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getAction() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.pacman.mypackage).
 */
public class ComputeDistance extends Controller<MOVE>
{
	private MOVE myMove=MOVE.NEUTRAL;
	private HashMap<Integer,HashMap<Integer,HashMap<MOVE,MOVE>>> pathNotReverse;
	private HashMap<Integer,HashMap<Integer,MOVE>> pathReverse;
	private int[] iIndex;
	
	public ComputeDistance (){
		pathNotReverse = new HashMap<Integer,HashMap<Integer,HashMap<MOVE,MOVE>>>();
		pathReverse = new HashMap<Integer,HashMap<Integer,MOVE>>();
	}
	
	public MOVE getMove(Game game, long timeDue) 
	{
		iIndex = game.getActivePillsIndices();
		try {
			File f = new File("PathNotReverse.ser");
			if(!f.exists()) { 
				FileOutputStream fout = new FileOutputStream(f);
				ObjectOutputStream oos = new ObjectOutputStream(fout);
				for(int i= 0; i<iIndex.length; i++){
					HashMap<Integer,HashMap<MOVE,MOVE>> dest = new HashMap<Integer,HashMap<MOVE,MOVE>>();
					for(int j= 0; j<iIndex.length; j++){
						MOVE[] moves = game.getPossibleMoves(iIndex[i]);
						HashMap<MOVE,MOVE> l1 = new HashMap<MOVE,MOVE>();
						for(MOVE move : moves){
							System.out.println(iIndex[i] + " " + iIndex[j]);
							MOVE res = game.getApproximateNextMoveTowardsTarget(iIndex[i], iIndex[j], move, DM.PATH);		
							l1.put(move, res);
						}
						dest.put(iIndex[j], l1);
					}
					pathNotReverse.put(iIndex[i], dest);
				}	

				oos.writeObject(pathNotReverse);

			}else{
				FileInputStream fin = new FileInputStream(f);
				ObjectInputStream ois = new ObjectInputStream(fin);
				pathNotReverse = (HashMap<Integer, HashMap<Integer, HashMap<MOVE, MOVE>>>) ois.readObject();
			}

			f = new File("PathReverse.ser");
			if(!f.exists()) { 
				FileOutputStream fout = new FileOutputStream(f);
				ObjectOutputStream oos = new ObjectOutputStream(fout);
				for(int i= 0; i<iIndex.length; i++){
					HashMap<Integer,MOVE> dest = new HashMap<Integer,MOVE>();
					for(int j= 0; j<iIndex.length; j++){
						MOVE move = game.getNextMoveTowardsTarget(iIndex[i], iIndex[j], DM.PATH);
						dest.put(iIndex[j], move);
					}
					pathReverse.put(iIndex[i], dest);
				}	
			}else{
				FileInputStream fin = new FileInputStream(f);
				ObjectInputStream ois = new ObjectInputStream(fin);
				pathReverse = (HashMap<Integer, HashMap<Integer, MOVE>>) ois.readObject();
			}
		} catch (IOException | ClassNotFoundException e) {e.printStackTrace();}
		
		return MOVE.NEUTRAL;
	}
}
