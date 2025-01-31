package org.firstinspires.ftc.teamcode.freeWifi.Robot

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.Servo

class Lift(private val robot: Robot, private val claw: Arm_Claw) {
    private val liftRotation: DcMotor = robot.hardwareMap.get(DcMotor::class.java, "lift_rotation")
    private val liftActuator: DcMotor = robot.hardwareMap.get(DcMotor::class.java, "lift_actuator")

    private var end_game_hold: Boolean = false;
    private var hold_lock: Boolean = false;

    fun init() {
        liftRotation.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        liftActuator.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
    }

    fun tick() {
        robot.telemetry.addData("liftRotation.position", liftRotation.currentPosition)
        robot.telemetry.addData("liftActuator.position", liftActuator.currentPosition)


        if (robot.opMode.gamepad2.left_trigger > 0.1) {
            liftRotation.power = -1.0;
            //claw.clawOpen = false;
        } else if (robot.opMode.gamepad2.right_trigger > 0.1) {
            liftRotation.power = 1.0;
            //claw.clawOpen = false;
        } else if (end_game_hold) {
            liftRotation.power = 1.0;
            //claw.clawOpen = false;
        } else {
            liftRotation.power = 0.0;
        }

        if (robot.opMode.gamepad2.b && !hold_lock) {
            end_game_hold = !end_game_hold;
            hold_lock = true;
        } else if (!robot.opMode.gamepad2.b) {
            hold_lock = false;
        }

        if (robot.opMode.gamepad2.y) {
            claw.clawOpen = false;
            liftActuator.power = 1.0;
        } else if (robot.opMode.gamepad2.x) {
            claw.clawOpen = false;
            liftActuator.power = -1.0;
        } else if (end_game_hold) {
            claw.clawOpen = false;
            liftActuator.power = 1.0;
        } else {
            liftActuator.power = 0.0;
        }
    }
}