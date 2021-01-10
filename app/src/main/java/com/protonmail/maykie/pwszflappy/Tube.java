package com.protonmail.maykie.pwszflappy;

import android.graphics.Point;

public class Tube {

    private final Point botTubePeakLocation;

    public Point getBotTubePeakLocation() {
        return botTubePeakLocation;
    }

    public Tube(Point botTubePeakLocation) {
        this.botTubePeakLocation = botTubePeakLocation;
    }
}
