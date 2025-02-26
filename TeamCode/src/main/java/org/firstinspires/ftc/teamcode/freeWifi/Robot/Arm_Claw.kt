package org.firstinspires.ftc.teamcode.freeWifi.Robot

import com.qualcomm.robotcore.hardware.Servo

private enum class CurrentClawPos {
    POS_LOW,
    POS_MID,
    POS_HIGH
}

class Arm_Claw(private val robot: Robot, private val lift: Arm_Lift) {
    private val armMotor: Servo = robot.hardwareMap.get(Servo::class.java, "claw_motor")
    private val armMotor2: Servo = robot.hardwareMap.get(Servo::class.java, "claw_motor2")
    private val clawRotation: Servo = robot.hardwareMap.get(Servo::class.java, "claw_rot")
    private val clawServo: Servo = robot.hardwareMap.get(Servo::class.java, "claw")
    public var clawOpen: Boolean = false
        set(value) {
            if (clawOpen) {
                clawServo.position = 0.9;
            } else {
                clawServo.position = 0.21;
            }

            field = value
        }

    // This is for the CLAW portion, this is used for the
    // dynamic adjustments based off of the armMotor position
    private var clawGroundPos: Double = 0.45;
    private var clawBucketPos: Double = 0.0;
    private var clawRange: Double = clawGroundPos - clawBucketPos;

    // This is for the ARM portion of the claw mechanism
    // NOT to be confused with the CLAW portion of the mechanism
    // Nice one.
    private var clawArmBucketPos: Double = 0.66;
    private var clawArmGroundPos: Double = 0.29;
    private var clawArmRange: Double = clawArmBucketPos - clawArmGroundPos;


    private var toggle_lock: Boolean = false;

    private var start_time: Long = 0;
    private var claw_move_timer: Long = 1000;
    //private var initialized = armMotor.position > clawArmBucketPos;
    private var initialized = true;

    private var time: Long = System.currentTimeMillis();

    fun init() {
        if (!initialized) {
            lift.doing_unfurl = true;
        }
        armMotor.direction = Servo.Direction.REVERSE
    }

    // The pos is in reference to the armMotor.
    // NOT armMotor2
    fun move_to_pos(pos: Double) {
        armMotor.position = pos;
        armMotor2.position = pos;
    }

    fun tick() {
        val delta: Double = (System.currentTimeMillis() - time) / 1000.0;
        time = System.currentTimeMillis();
        robot.telemetry.addData("ArmMotor.position", armMotor.position);
        robot.telemetry.addData("ArmMotor2.position", armMotor2.position);

        //if (!initialized) {
        if (false) {
            lift.doing_unfurl = true;
            if (start_time == 0.toLong()) {
                start_time = System.currentTimeMillis();
            }
            lift.liftMotor.power = 1.0;

            claw_move_timer = System.currentTimeMillis() - start_time;
            robot.telemetry.addData("claw time", claw_move_timer)
            if (claw_move_timer >= 1000) {
                lift.liftMotor.power = 0.0
                move_to_pos(clawArmBucketPos - 0.2)
                initialized = true;
                lift.doing_unfurl = false;
            } else {
                return;
            }
        }

        //if (clawRotation.position > clawGroundPos) {
        //    clawRotation.position = clawGroundPos
        //}

        // Limit the arms rotation to the bucket max or the
        // ground max.
        if (armMotor.position > clawArmBucketPos || armMotor2.position > clawArmBucketPos) {
            move_to_pos(clawArmBucketPos)
        } else if (armMotor.position < clawArmGroundPos || armMotor2.position < clawArmGroundPos) {
            move_to_pos(clawArmGroundPos)
        }

        robot.telemetry.addData("clawRotation.position", clawRotation.position);
        robot.telemetry.addData("delta", delta);

        if (robot.opMode.gamepad2.left_bumper) {
            armMotor.position += 0.5 * delta;
            armMotor2.position = armMotor.position;
        } else if (robot.opMode.gamepad2.right_bumper) {
            armMotor.position -= 0.5 * delta;
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
        var arm_rot_percent: Double = armMotor.position / clawArmRange
        var claw_rot: Double = clawRange * arm_rot_percent;

        // Flipped at some point, woopsies I guess?
        clawRotation.position = 1.0 - claw_rot;

        robot.telemetry.addData("clawRotation.position", clawRotation.position);
    }
}