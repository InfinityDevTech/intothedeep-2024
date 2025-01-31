package org.firstinspires.ftc.teamcode.freeWifi.Robot

import com.qualcomm.robotcore.hardware.*

class Initialize(private val robot: Robot) {
    fun init() {
        robot.halfTimeRumble = Gamepad.RumbleEffect.Builder().addStep(1.0, 1.0, 200).addStep(0.0, 0.0, 200).addStep(1.0, 1.0, 200).build()
        robot.endGameRumble = Gamepad.RumbleEffect.Builder().addStep(1.0, 0.0, 200).addStep(0.0, 1.0, 200).addStep(1.0, 0.0, 200).addStep(0.0, 1.0, 200).addStep(1.0, 0.0, 200).build()

        robot.motors = hashMapOf(
            Motors.LeftFront to robot.hardwareMap.get(DcMotor::class.java, "front_left"),
            Motors.RightFront to robot.hardwareMap.get(DcMotor::class.java, "front_right"),
            Motors.LeftBack to robot.hardwareMap.get(DcMotor::class.java, "back_left"),
            Motors.RightBack to robot.hardwareMap.get(DcMotor::class.java, "back_right"),
        )

        //robot.motors[Motors.LeftBack]?.direction = DcMotorSimple.Direction.REVERSE;
        //robot.motors[Motors.RightBack]?.direction = DcMotorSimple.Direction.REVERSE;

        robot.servos = hashMapOf();

        robot.telemetry.addLine("[ROBOT]: Initialized all Motors, Servos, and Sensors")

        robot.currentState = "Initialized";
    }
}