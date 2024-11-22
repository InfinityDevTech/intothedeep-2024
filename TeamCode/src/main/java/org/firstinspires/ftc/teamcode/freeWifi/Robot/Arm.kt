package org.firstinspires.ftc.teamcode.freeWifi.Robot

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo

class Arm(private val robot: Robot) {
    private val armMotor: DcMotor = robot.hardwareMap.get(DcMotor::class.java, "arm_motor")
    private val armMotor2: DcMotor = robot.hardwareMap.get(DcMotor::class.java, "arm_motor2")
    private val extendMotor: DcMotor = robot.hardwareMap.get(DcMotor::class.java, "extend_motor")
    private val liftMotor: DcMotor = robot.hardwareMap.get(DcMotor::class.java, "lift_motor")

    private val intakeServo: Servo = robot.hardwareMap.get(Servo::class.java, "intake_servo")

    fun init() {
        armMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        //12armMotor2.direction = DcMotorSimple.Direction.REVERSE
        liftMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        extendMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
    }

    fun read_input() {
        if (robot.opMode.gamepad2.left_bumper) {
            armMotor.power = 0.5
            armMotor2.power = 0.5
        } else if (robot.opMode.gamepad2.right_bumper) {
            armMotor.power = -0.5
            armMotor2.power = -0.5
        } else {
            armMotor.power = 0.0
            armMotor2.power = 0.0
        }

        if (robot.opMode.gamepad2.dpad_up) {
            liftMotor.power = 0.5
        } else if (robot.opMode.gamepad2.dpad_down) {
            liftMotor.power = -0.5
        } else {
            liftMotor.power = 0.0
        }

        if (robot.opMode.gamepad2.a) {
            intakeServo.position = 0.0
        } else if (robot.opMode.gamepad2.b) {
            intakeServo.position = 1.0
        }

        if (robot.opMode.gamepad2.right_trigger > 0.5) {
            extendMotor.power = 0.5
        } else if (robot.opMode.gamepad2.left_trigger > 0.5) {
            extendMotor.power = -0.5
        } else {
            extendMotor.power = 0.0
        }
    }
}