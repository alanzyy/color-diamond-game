/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zyy.diamond.color;

import sun.audio.AudioPlayer;

class SoundTest {

    public static void loadYes() {
        AudioPlayer.player.start(SoundTest.class.getResourceAsStream("resources/yes.wav"));
    }

    public static void loadNo() {
        AudioPlayer.player.start(SoundTest.class.getResourceAsStream("resources/no.wav"));
    }
}