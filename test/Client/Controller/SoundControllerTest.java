/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.Controller;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ramòn Janssen
 */
public class SoundControllerTest {
    
    public SoundControllerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of play method, of class SoundController.
     */
    @Test
    public void testPlay() {
        System.out.println("playBUTTONPRESS");
        SoundController.SoundFile audioFile = SoundController.SoundFile.BUTTONPRESS;
        SoundController.play(audioFile);
        
        System.out.println("play ENDTURN");
        audioFile = SoundController.SoundFile.ENDTURN;
        SoundController.play(audioFile);
        
        System.out.println("play HEAL");
        audioFile = SoundController.SoundFile.HEAL;
        SoundController.play(audioFile);
        
        System.out.println("play MAGICALATTACK");
        audioFile = SoundController.SoundFile.MAGICALATTACK;
        SoundController.play(audioFile);
        
        System.out.println("play MAGICALBLOCK");
        audioFile = SoundController.SoundFile.MAGICALBLOCK;
        SoundController.play(audioFile);
        
        System.out.println("play MINION");
        audioFile = SoundController.SoundFile.MINION;
        SoundController.play(audioFile);
        
        System.out.println("play PHYSICALATTACK");
        audioFile = SoundController.SoundFile.PHYSICALATTACK;
        SoundController.play(audioFile);
        
        System.out.println("play PHYSICALBLOCK");
        audioFile = SoundController.SoundFile.PHYSICALBLOCK;
        SoundController.play(audioFile);
        
        System.out.println("play STARTTURN");
        audioFile = SoundController.SoundFile.STARTTURN;
        SoundController.play(audioFile);
        
    }
    
}
