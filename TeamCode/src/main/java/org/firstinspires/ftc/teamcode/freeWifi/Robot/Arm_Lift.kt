package org.firstinspires.ftc.teamcode.freeWifi.Robot

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.Servo

enum class LiftPositions {
    DownPosition,
    MidPosition,
    HighPosition
}

class Arm_Lift(private val robot: Robot) {
    private val liftMotor: DcMotor = robot.hardwareMap.get(DcMotor::class.java, "lift_motor")
    public val bucketServo: Servo = robot.hardwareMap.get(Servo::class.java, "lift_rot")

    private val mid_point: Int = 2150;
    private val high_point: Int = 4250;
    private var arm_motion_lock: Boolean = false;

    private var targeted_pos = 0;

    public var current_lift_position = LiftPositions.DownPosition
        set(value) {
            when (value){
                LiftPositions.DownPosition -> {
                    targeted_pos = 0;
                }

                LiftPositions.MidPosition -> {
                    targeted_pos = mid_point;
                }

                LiftPositions.HighPosition -> {
                    targeted_pos = high_point;
                }
            }

            field = value;
        }

    // Public getter and setter to set the bucket pos.
    public var bucket_emptying: Boolean = false
        set(value) {
            if (!value) {
                bucketServo.position = 1.0;
            } else {
                bucketServo.position = 0.5;
            }

            field = value;
        }
    private var bucket_empty_lock: Boolean = false;


    fun init() {
        //liftMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE;
        liftMotor.targetPosition = 100;
        liftMotor.mode = DcMotor.RunMode.RUN_TO_POSITION;
        //bucket_emptying = false;
    }

    fun raise_lift() {
        when (current_lift_position) {
            LiftPositions.DownPosition -> {
                current_lift_position = LiftPositions.MidPosition;
            }
            LiftPositions.MidPosition -> {
                current_lift_position = LiftPositions.HighPosition;
            }
            else -> {}
        }
    }

    fun lower_lift() {
        when (current_lift_position) {
            LiftPositions.MidPosition -> {
                current_lift_position = LiftPositions.DownPosition
            }
            LiftPositions.HighPosition -> {
                current_lift_position = LiftPositions.MidPosition
            }

            else -> {}
        }
    }

    fun read_input() {
        //liftMotor.power = 0.0;
        liftMotor.targetPosition = targeted_pos;
        liftMotor.power = 1.0;

        robot.telemetry.addData("liftMotor.position", liftMotor.currentPosition);

        if (!arm_motion_lock) {
            if (robot.opMode.gamepad2.dpad_up) {
                raise_lift();
                arm_motion_lock = true;
            } else if (robot.opMode.gamepad2.dpad_down) {
                lower_lift();
                arm_motion_lock = true;
            }
        } else if (!robot.opMode.gamepad2.dpad_up && !robot.opMode.gamepad2.dpad_down) {
            arm_motion_lock = false;
        }
        /*
        if (robot.opMode.gamepad2.dpad_up) {
            liftMotor.power = -1.0;
        } else if (robot.opMode.gamepad2.dpad_down) {
            liftMotor.power = 1.0;
        }*/

        robot.telemetry.addData("lift_rotationary_state", current_lift_position)

        robot.telemetry.addData("intakeServo.posiiton", bucketServo.position);

        // The purpose of bucket_empty_lock is to lock the input if the robot ticks
        // faster than the human can release the button.
        // bucket_emptying is a metric to see the current statee

        if (!bucket_empty_lock && robot.opMode.gamepad2.left_stick_button) {
            bucket_emptying = !bucket_emptying
            bucket_empty_lock = true;
        } else if (!robot.opMode.gamepad2.left_stick_button) {
            bucket_empty_lock = false;
        }
    }
}