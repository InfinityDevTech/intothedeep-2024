package org.firstinspires.ftc.teamcode.freeWifi.Robot

import com.qualcomm.robotcore.hardware.Servo

class Arm_Claw(private val robot: Robot, private val lift: Arm_Lift) {
    private val armMotor: Servo = robot.hardwareMap.get(Servo::class.java, "claw_motor")
    private val armMotor2: Servo = robot.hardwareMap.get(Servo::class.java, "claw_motor2")
    private val clawRotation: Servo = robot.hardwareMap.get(Servo::class.java, "claw_rot")
    private val clawServo: Servo = robot.hardwareMap.get(Servo::class.java, "claw")
    private var clawOpen: Boolean = false
        set(value) {
            if (clawOpen) {
                clawServo.position = 0.75;
            } else {
                clawServo.position = 0.21;
            }

            field = value
        }

    private var arm_start_rotation: Double = 1.0;
    private var arm_unload_rotation: Double = 0.62;

    private var claw_rot_max: Double = 0.62;

    private var claw_ground_pos: Double = 0.244;
    private var claw_bucket_pos: Double = 0.659;
    private var claw_range: Double = claw_bucket_pos - claw_ground_pos;

    private var arm_ground_limit: Double = 0.2267;
    private var arm_bucket_limit: Double = 0.6517;
    private var arm_range: Double = arm_bucket_limit - arm_ground_limit;

    private var toggle_lock: Boolean = false;

    private var input_lock: Boolean = true;
    private var claw_move_timer: Long = 1000;
    private var initialized = armMotor.position <= arm_bucket_limit;

    fun init() {
        //armMotor2.direction = Servo.Direction.REVERSE
        armMotor.direction = Servo.Direction.REVERSE
        //clawOpen = true;
        //armMotor.position = 0.0;
        //armMotor2.position = 0.0;

        //move_to_pos(arm_start_rotation);
    }

    // The pos is in reference to the armMotor.
    // NOT armMotor2
    fun move_to_pos(pos: Double) {
        armMotor.position = pos;
        armMotor2.position = pos;
    }

    fun unfurl_claw() {
        lift.bucket_emptying = true;

        move_to_pos(arm_bucket_limit)
    }

    fun tick(delta_time: Long) {
        robot.telemetry.addData("ArmMotor.position", armMotor.position);
        robot.telemetry.addData("ArmMotor2.position", armMotor2.position);

        if (!initialized) {
            lift.bucket_emptying = true;

            claw_move_timer -= delta_time;
            if (claw_move_timer <= 0) {
                move_to_pos(arm_bucket_limit);
                initialized = true;
            }

            return;
        }

        if (clawRotation.position > claw_rot_max) {
            clawRotation.position = claw_rot_max
        }

        // Limit the arms rotation to the bucket max or the
        // ground max.
        if (armMotor.position > arm_bucket_limit || armMotor2.position > arm_bucket_limit) {
            move_to_pos(arm_bucket_limit)
        } else if (armMotor.position < arm_ground_limit || armMotor2.position < arm_ground_limit) {
            move_to_pos(arm_ground_limit)
        }

        if (robot.opMode.gamepad2.x) {
            clawRotation.position += 0.005;
        } else if (robot.opMode.gamepad2.y) {
            clawRotation.position -= 0.005;
        }

        robot.telemetry.addData("clawRotation.position", clawRotation.position);

        if (robot.opMode.gamepad2.left_bumper) {
            armMotor.position += 0.005;
            armMotor2.position = armMotor.position;
        } else if (robot.opMode.gamepad2.right_bumper) {
            armMotor.position -= 0.005;
            armMotor2.position = armMotor.position
        }

        robot.telemetry.addData("clawServo.position", clawServo.position);

        if (robot.opMode.gamepad2.a && !toggle_lock) {
            clawOpen = !clawOpen
            toggle_lock = true;
        } else if (!robot.opMode.gamepad2.a) {
            toggle_lock = false;
        }

        // Calculate the current % we are along the range
        var arm_rot_percent: Double = armMotor.position / arm_range
        var claw_rot: Double = claw_range * arm_rot_percent;

        clawRotation.position = claw_rot;
    }
}