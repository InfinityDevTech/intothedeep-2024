package org.firstinspires.ftc.teamcode.freeWifi.DriveModes

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Gamepad
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.teamcode.freeWifi.RR.SampleMecanumDrive
import org.firstinspires.ftc.teamcode.freeWifi.Robot.*
import org.firstinspires.ftc.vision.VisionPortal
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor


@TeleOp(name = "Mecanum", group = "TelPrimary")
class Mecanum : LinearOpMode() {

    val robot = Robot(this);

    private var aprilTag: AprilTagProcessor? = null

    private var visionPortal: VisionPortal? = null

    private var fast_mode: Boolean = false;

    // INIT
    override fun runOpMode() {
        // Start the pipeline
        //initAprilTag();

        // Variables
        val gamepad1: Gamepad = this.gamepad1;
        val gamepad2: Gamepad = this.gamepad2;
        var lastTime: Long = System.currentTimeMillis();
        var speed_pressed = false;

        robot.init();

        val armLift = Arm_Lift(robot);
        val armClaw = Arm_Claw(robot, armLift);
        val lift = Lift(robot, armClaw);
        val smd = SampleMecanumDrive(robot);

        armClaw.init();
        armLift.init();
        lift.init();

        telemetry.addLine("[ROBOT]: " + robot.currentState);
        telemetry.update();

        // Wait for the start to press
        waitForStart();

        //var last_tick: Long = System.currentTimeMillis();

        // NO VARIABLES IN HERE PERSIST. ITS PER LOOP!
        while (opModeIsActive()) {
            // Time in between loops
            // Used for time dependent things, like timers!
            //val delta_time: Long = System.currentTimeMillis() - last_tick;

            smd.update();
            armClaw.tick()
            lift.tick();
            armLift.tick()

            val pose = smd.poseEstimate;

            telemetry.addData("x", pose.x);
            telemetry.addData("y", pose.y);
            telemetry.addData("heading", pose.heading);
            var speed = 0.7;

            // Per loop variable initialization
            /*if (gamepad1.left_bumper && !speed_pressed) {
                fast_mode = !fast_mode
                speed_pressed = true
            } else if (!gamepad1.left_bumper && speed_pressed) {
                speed_pressed = false
            }*/

            if (fast_mode) speed = 1.0;

            val vertical: Float = -gamepad1.left_stick_y
            val horizontal: Float = gamepad1.left_stick_x
            val rotation: Float = gamepad1.right_stick_x

            robot.setMotorPower(Motors.LeftFront, (-rotation + vertical + horizontal) * speed)
            robot.setMotorPower(Motors.RightFront, (rotation + vertical + -horizontal) * speed)
            robot.setMotorPower(Motors.LeftBack, (-rotation + vertical + -horizontal) * speed)
            robot.setMotorPower(Motors.RightBack, (rotation + vertical + horizontal) * speed)

            val det = aprilTag?.detections;

            if (det?.size != 0) {
                telemetry.addData("Detections", det?.size);
                telemetry.addData("Detected: ", det?.first()?.id)
            }

            telemetry.update();
        }
    }

    private fun initAprilTag() {

        // Create the AprilTag processor the easy way.
        aprilTag = AprilTagProcessor.easyCreateWithDefaults()

        // Create the vision portal the easy way.
            visionPortal = VisionPortal.easyCreateWithDefaults(
                hardwareMap[WebcamName::class.java, "Webcam 1"], aprilTag
            )
    }
}