package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.rowlandhall.meepmeep.MeepMeep;
import org.rowlandhall.meepmeep.roadrunner.DefaultBotBuilder;
import org.rowlandhall.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(500);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(new Pose2d(-35, -60, Math.toRadians(90)))
                        .lineToLinearHeading(new Pose2d(-53, -52, Math.toRadians(45)))
                        .back(3.0)
                        .lineToLinearHeading(new Pose2d(-36, -40, Math.toRadians(90)))
                        .forward(30.0)
                        .strafeLeft(7.0)
                        .lineToLinearHeading(new Pose2d(-53, -50, Math.toRadians(45)))
                        .back(3.0)
                        .lineToLinearHeading(new Pose2d(56, -52, Math.toRadians(90)))
                        .build());


        meepMeep.setBackground(MeepMeep.Background.FIELD_INTOTHEDEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}