package boarderlingothegame;

import java.awt.Point;
import java.awt.Polygon;

public interface Collidable {
	/**
	 * getLocation sollte, soweit m�glich dein am weitesten links oben liegenden Punkt zur�ckgeben
	 * 
	 * @return Point in Screen Coordinates
	 */
	Point getLocation();
	/**
	 * getHitBox ist einfach die Fl�che, bei deren Eintritt eine Kollision des eintretenden Objektes getriggert wird
	 * 
	 * @return Polygon
	 */
	Polygon getHitBox();
}
