package org.firstinspires.ftc.teamcode.freeWifi.Autonomous

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.trajectory.constraints.AngularVelocityConstraint
import com.acmerobotics.roadrunner.trajectory.constraints.MinVelocityConstraint
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryVelocityConstraint
import com.acmerobotics.roadrunner.trajectory.constraints.TranslationalVelocityConstraint
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.firstinspires.ftc.teamcode.freeWifi.RR.SampleMecanumDrive
import org.firstinspires.ftc.teamcode.freeWifi.Robot.Arm_Claw
import org.firstinspires.ftc.teamcode.freeWifi.Robot.Arm_Lift
import org.firstinspires.ftc.teamcode.freeWifi.Robot.Motors
import org.firstinspires.ftc.teamcode.freeWifi.Robot.Robot
import java.util.Arrays

@Autonomous(name = "RedLeftAuto", group = "TelPrimary")
class ParkRight : LinearOpMode() {
    val robot = Robot(this);

    override fun runOpMode() {
        robot.init();

        //robot.motors[Motors.LeftBack]?.direction = DcMotorSimple.Direction.FORWARD;
        var mecanum = SampleMecanumDrive(robot);
        var lift = Arm_Lift(robot);
        var claw = Arm_Claw(robot, lift);

        waitForStart();

        mecanum.poseEstimate = Pose2d(-35.0, -60.0, Math.toRadians(90.0))

        var auto_drive = mecanum.trajectorySequenceBuilder(mecanum.poseEstimate);
        var built = auto_drive
            .lineToLinearHeading(Pose2d(-53.0, -52.0, Math.toRadians(45.0)))
            .back(3.0)
            .lineToLinearHeading(Pose2d(-33.0, -40.0, Math.toRadians(90.0)))
            .forward(30.0)
            .strafeLeft(7.0)
            .lineToLinearHeading(Pose2d(-53.0, -50.0, Math.toRadians(45.0)))
            .back(3.0)
            .build();

        mecanum.followTrajectorySequence(built);

        while (opModeIsActive()) {
            mecanum.update()
            robot.telemetry.update()
            telemetry.update()
        }
    }
}