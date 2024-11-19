package org.firstinspires.ftc.teamcode.freeWifi.RR.opmode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.freeWifi.RR.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.freeWifi.Robot.Robot;

/*
 * This is a simple routine to test turning capabilities.
 */
@Config
//@Disabled
@Autonomous(group = "drive")
public class TurnTest extends LinearOpMode {
    public static double ANGLE = 90; // deg

    @Override
    public void runOpMode() throws InterruptedException {
        Robot robot = new Robot(this).init();

        SampleMecanumDrive drive = new SampleMecanumDrive(robot);

        waitForStart();

        if (isStopRequested()) return;

        drive.turn(Math.toRadians(ANGLE));
    }
}
