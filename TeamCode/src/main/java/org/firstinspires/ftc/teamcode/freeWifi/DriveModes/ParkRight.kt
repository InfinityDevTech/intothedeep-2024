package org.firstinspires.ftc.teamcode.freeWifi.DriveModes

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.freeWifi.RR.SampleMecanumDrive
import org.firstinspires.ftc.teamcode.freeWifi.Robot.Arm_Claw
import org.firstinspires.ftc.teamcode.freeWifi.Robot.Arm_Lift
import org.firstinspires.ftc.teamcode.freeWifi.Robot.Robot

@Autonomous(name = "RedLeftAuto", group = "TelPrimary")
class ParkRight : LinearOpMode() {
    val robot = Robot(this);

    override fun runOpMode() {
        robot.init();
        var mecanum = SampleMecanumDrive(robot);
        var lift = Arm_Lift(robot);
        var claw = Arm_Claw(robot, lift);

        mecanum.poseEstimate = Pose2d(20.0, -60.0, Math.toRadians(90.0))

        var auto_drive = mecanum.trajectorySequenceBuilder(mecanum.poseEstimate);

        /*auto_drive.lineToSplineHeading(Pose2d(-55.0, -53.0, Math.toRadians(45.0)))
            .addTemporalMarker {

            }
            .waitSeconds(20.0)
            .lineToSplineHeading(Pose2d(-52.0, -45.0, Math.toRadians(90.0)))*/
        auto_drive
            .forward(0.5)
            .waitSeconds(20.0)
            .strafeRight(300000.0)

        var built = auto_drive.build();

        waitForStart();

        mecanum.followTrajectorySequence(built);

        while (opModeIsActive()) {

        }
    }
}