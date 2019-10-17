package bot;

import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.location.Coordinate;

public class LooterAreas {
    Area area= new Area.Rectangular(new Coordinate(3083,3538,0),new Coordinate(3096,3530,0));
    Area safeArea= new Area.Rectangular(new Coordinate(3087,3525,0),new Coordinate(3097,3523,0));
    Area bankArea= new Area.Rectangular(new Coordinate(3091,3498),new Coordinate(3099,3487));
    Area BesideTheDitch= new Area.Rectangular(new Coordinate(3078,3521), new Coordinate(3115,3515));
}
