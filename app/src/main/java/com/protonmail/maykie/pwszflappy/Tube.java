package com.protonmail.maykie.pwszflappy;

import android.graphics.Point;

public class Tube {

    private Point botTubePeakLocation;

    public Point getBotTubePeakLocation() {
        return botTubePeakLocation;
    }

    public void setBotTubePeakLocation(Point botTubePeakLocation) {
        this.botTubePeakLocation = botTubePeakLocation;
    }

    public Tube(Point botTubePeakLocation) {
        this.botTubePeakLocation = botTubePeakLocation;
    }
}
