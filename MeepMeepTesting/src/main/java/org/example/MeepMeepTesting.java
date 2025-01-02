package org.example;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import com.acmerobotics.roadrunner.geometry.Vector2d;
import org.rowlandhall.meepmeep.MeepMeep;
import org.rowlandhall.meepmeep.roadrunner.DefaultBotBuilder;
import org.rowlandhall.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(400);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 16.5)
                .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(new Pose2d(-35, -60, Math.toRadians(90)))
                        .lineToSplineHeading(new Pose2d(-55, -53, Math.toRadians(45)))
                        .lineToSplineHeading(new Pose2d(-52, -45, Math.toRadians(90)))
                        //.turn(Math.toRadians(90))
                        //.forward(30)
                        //.turn(Math.toRadians(90))
                        //.forward(30)
                        //.turn(Math.toRadians(90))
                        //.forward(30)
                        //.turn(Math.toRadians(90))
                        .build());


        meepMeep.setBackground(MeepMeep.Background.FIELD_INTOTHEDEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}