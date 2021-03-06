package boarderlingothegame;

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JOptionPane;

import boarderlingothegame.sprites.Cactus;
import boarderlingothegame.sprites.Granny;
import boarderlingothegame.sprites.Heli;
import boarderlingothegame.sprites.Obstacle;
import boarderlingothegame.sprites.Player;

public class ObstacleController {
	
	private List<Obstacle> obstacles;
	private static ObstacleController singleton;
	public boolean isCollided = false;
	
	private ObstacleController() {};
	
	public static synchronized ObstacleController getInstance() {
		if (singleton == null)
			singleton=new ObstacleController();
		return singleton;
	}
	
	public void reset() {
		obstacles  = new ArrayList<>();
	}
	public void collide(Polygon hitBox) {
		doForAllObstacles(e->collisionDetection(e,hitBox));
	}
	private void collisionDetection(Obstacle eObst, Polygon hitbox) {
		if( eObst.getHitBox().intersects(hitbox.getBounds()) ){
		    Area collision = new Area(eObst.getHitBox());
		    collision.intersect(new Area(hitbox));
		    if(!collision.isEmpty()){
				JOptionPane.showMessageDialog(null,"AUA (Gel�hmt gar quer)");
				System.out.println(eObst.getSpawnedBy());
				reset();
				isCollided = true;
				return;
		    }
		}
	}
	
	private synchronized void doForAllObstacles(Consumer<Obstacle> function) {
		for (Iterator<Obstacle> iterator = getObstacles().iterator(); iterator.hasNext();) {
			try {
			    Obstacle eObst = iterator.next();
			    function.accept(eObst);
			} catch (Exception exc) {
				System.out.println("\u001B[31m" + exc.getMessage() + "\u001B[0m");
			}
		}
	}
	public synchronized void add(String order, String nameOfPurchaser) {
		if(order.toUpperCase().contains("KAKTUS"))
			getObstacles().add(new Cactus(nameOfPurchaser));
		if(order.toUpperCase().contains("HELI"))
			getObstacles().add(new Heli(nameOfPurchaser));
		if(order.toUpperCase().contains("OMA"))
			getObstacles().add(new Granny(nameOfPurchaser));
	}
	private synchronized void removeInvalidObjects() {

		for (Iterator<Obstacle> iterator = getObstacles().iterator(); iterator.hasNext();) {
		    Obstacle eObst = iterator.next();
		    if(eObst.getLocation().x <= -150) {
		        iterator.remove();
		    }
		}
	}
	private List<Obstacle> getObstacles() {
		return obstacles;
	}



	public void autoScroll(int factor, int playerSpeed) {
		final int f = factor;
		doForAllObstacles(eObst -> moveObstacles(f,playerSpeed, eObst));	
		removeInvalidObjects();
	}
	private void moveObstacles(int factor, int playerSpeed, Obstacle eObst) {
		if(eObst.getLocation().x > -150) {
			if (eObst instanceof Heli)
				eObst.moveRight(10);
			if (eObst instanceof Granny) {//Dringend refactoren!!
				((Granny)eObst).moveDown();
			}
			eObst.moveRight(playerSpeed*factor);
		}
	}

	public void draw(Graphics2D g2d, GamePanel gamePanel) {
		doForAllObstacles(eObst -> drawObstacle(eObst, g2d, gamePanel));
		
		
	}

	private void drawObstacle(Obstacle eObst, Graphics2D g2d, GamePanel gamePanel) {
		g2d.drawImage(eObst.getImage(AnimationTimer.getInstance().getFrame()),eObst.getLocation().x,eObst.getLocation().y,gamePanel);
		g2d.drawString(eObst.getSpawnedBy(), eObst.getLocation().x,eObst.getLocation().y);
	}
}
