package bot;

import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.location.navigation.basic.BresenhamPath;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.GroundItems;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.LoopingBot;

public class Looter extends LoopingBot {

    int adamantArrow=60;
    int x=0;
    boolean gotoCenter=false;
    boolean clickOnDitch=false;
    boolean travelToZafeZone=false;

    private enum State
    {
        LOOT
    }

    @Override
    public void onLoop() {
        switch (getCurrentState()) {
            case LOOT:

        LooterAreas areas= new LooterAreas();
        LooterApi api= new LooterApi();
        try {
                    if (Inventory.isEmpty() && !areas.area.contains(Players.getLocal())) {
                        gotoCenter=true;

                     }
                } catch (NullPointerException e) {
                    System.out.println("Excepcion in taking arrow");
                }
                try{
                    if(Inventory.isEmpty() && areas.area.contains(Players.getLocal())){
                       api.pickUpArrows();
                    }

                }catch (NullPointerException e){

                }
                try {

                    if  ((Inventory.getItems("Adamant arrow").first().getQuantity() < adamantArrow) || Inventory.isEmpty()) {

                        if (Players.getLocal().getHealthGauge() == null && !Players.getLocal().isMoving()) {
                           api.pickUpArrows();
                            travelToZafeZone=true;
                            System.out.println("Grabbing arrows");
                        }

                    }
                } catch(NullPointerException e){
                        System.out.println("Nuller Eccce");
                    }

                if(gotoCenter){
                    gotoCenter = false;
                  api.travelTo(areas.area);
                }
                try {
                    if ( GroundItems.newQuery().names("Adamant arrow").results().nearest().distanceTo(Players.getLocal())>6&&Players.getLocal().getHealthGauge()==null &&!areas.area.contains(Players.getLocal())&& Inventory.getItems("Adamant arrow").first().getQuantity()<adamantArrow) {

                            api.travelTo(areas.area);
                        }} catch (NullPointerException e) {

                        }

                    System.out.println("Stuck");
                    if (Players.getLocal().getHealthGauge() != null) {
                        try {
                         api.travelTo(areas.safeArea);
                        } catch (NullPointerException e) {
                            System.out.println("Excepcion on running");
                        }
                    }
                    try {
                        if (areas.safeArea.contains(Players.getLocal()) && Players.getLocal().getHealthGauge().getPercent() < 50) {
                            Execution.delay(40000, 60000);
                        }
                    }catch (NullPointerException e){

                    }
                    try {
                        if ( travelToZafeZone && !areas.safeArea.contains(Players.getLocal()) && Inventory.getItems("Adamant arrow").first().getQuantity() >= adamantArrow) {
                            api.travelTo(areas.safeArea);
                            if(areas.BesideTheDitch.contains(Players.getLocal())){
                                travelToZafeZone=false;
                            }
                        }
                    }catch (NullPointerException e){

                    }
                    if(areas.BesideTheDitch.contains(Players.getLocal())|| areas.bankArea.contains(Players.getLocal())){
                        travelToZafeZone=false;
                    }
                    try {

                        if (GameObjects.newQuery().names("Wilderness Ditch").results() != null && areas.safeArea.contains(Players.getLocal()) && Inventory.getItems("Adamant arrow").first().getQuantity() >= adamantArrow) {
                           clickOnDitch=true;
                           travelToZafeZone=false;
                            if (Players.getLocal().getAnimationId() == -1 && !areas.bankArea.isReachable() && clickOnDitch) {
                                GameObjects.newQuery().names("Wilderness Ditch").results().nearest().interact("Cross");
                                Execution.delay(1000, 1500);
                                clickOnDitch=false;
                                System.out.println("clicking wilderness ditch");
                            }
                        }
                    }catch (NullPointerException e){
                        System.out.println("Click wilderness ditch to go to bank excepcion");
                    }
                    try {
                        if ( Npcs.newQuery().names("Banker").results().isEmpty() && areas.bankArea.isReachable() && Inventory.getItems("Adamant arrow").first().getQuantity() >= adamantArrow) {
                         api.travelTo(areas.bankArea);
                        }
                    }catch(NullPointerException e){
                        System.out.println("Going to safe zone error");
                    }
                    try {
                        if (!areas.bankArea.contains(Players.getLocal())&&!Inventory.isEmpty() && !Npcs.newQuery().names("Banker").results().isEmpty() && !Players.getLocal().isMoving()) {

                            BresenhamPath pathtobank = BresenhamPath.buildTo(Npcs.newQuery().names("Banker").results().nearest());
                            pathtobank.step(true);
                        }
                        if(!Inventory.isEmpty()) {
                            Npcs.newQuery().names("Banker").results().nearest().interact("Bank");
                            Execution.delay(1000, 2000);


                            Bank.depositAllExcept(0);
                            clickOnDitch = true;
                        }

                    }catch(NullPointerException e){
                        System.out.println("Clicking banker error");
                    }


                    if( Inventory.isEmpty() && !areas.BesideTheDitch.contains(Players.getLocal()) && !areas.area.isReachable() ) {
                       api.travelTo(areas.BesideTheDitch);
                    }

                if(Inventory.isEmpty() && areas.BesideTheDitch.contains(Players.getLocal())){
                    clickOnDitch=true;
                }

                try {
                    if (clickOnDitch && Inventory.isEmpty() && !areas.safeArea.contains(Players.getLocal())) {
                        GameObjects.newQuery().names("Wilderness Ditch").results().nearest().interact("Cross");
                        Execution.delay(2000, 3000);
                    }
                }catch (NullPointerException e){

                }
                if(Inventory.isEmpty() && areas.safeArea.contains(Players.getLocal())){
                    clickOnDitch=false;
                    api.travelTo(areas.area);
                }
                break;

        }
    }

    private State getCurrentState() {

        //Players.getLocal().getAnimationId() == -1 && !Players.getLocal().isMoving()
        {
            return State.LOOT;
        }
    }

}
