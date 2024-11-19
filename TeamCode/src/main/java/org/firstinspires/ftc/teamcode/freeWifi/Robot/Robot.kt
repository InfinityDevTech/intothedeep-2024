package org.firstinspires.ftc.teamcode.freeWifi.Robot

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.Gamepad.RumbleEffect
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.robotcore.external.Telemetry

enum class Motors {
    LeftFront,
    RightFront,
    LeftBack,
    RightBack,
}

interface IMovementComposable {
    fun run_movement();
}


enum class Servos {
}

enum class Sensors {}

class Robot(val opMode: OpMode) {
    var currentState = "Nothing"
    private val init = Initialize(this)

    lateinit var motors: HashMap<Motors, DcMotor>
    lateinit var servos: HashMap<Servos, Servo>
    lateinit var sensors: HashMap<Sensors, Any>

    lateinit var halfTimeRumble: RumbleEffect;
    lateinit var endGameRumble: RumbleEffect;

    val telemetry: Telemetry = opMode.telemetry;

    val hardwareMap: HardwareMap get() = opMode.hardwareMap
    val isActive get() = (opMode as LinearOpMode).opModeIsActive()

    fun init() : Robot {
        if (this.currentState == "Initialized") {
            telemetry.addLine("[ROBOT]: Multiple calls to init, ignoring...")

            return this;
        }
        init.init(); // Populate fields in this class.

        //this.motors[Motors.LeftFront]?.direction = DcMotorSimple.Direction.REVERSE;
        this.motors[Motors.RightFront]?.direction = DcMotorSimple.Direction.REVERSE;
        this.motors[Motors.LeftBack]?.direction = DcMotorSimple.Direction.REVERSE;

        telemetry.addLine("[ROBOT]: Initialized Robit");

        return this;
    }

    fun setMotorMode(motor: Motors, mode: DcMotor.RunMode) : Robot {
        this.motors[motor]?.mode = mode;
        return this;
    }

    fun setMotorPower(motor: Motors, power: Double) : Robot {
        this.motors[motor]?.power = power;
        return this;
    }

    fun setMotorsPower(power: Double, vararg motors: Motors) : Robot {
        for (motor in motors) {
            this.motors[motor]?.power = power;
        }
        return this;
    }

    fun setMotorsMode(mode: DcMotor.RunMode, vararg motors: Motors) : Robot {
        for (motor in motors) {
            setMotorMode(motor, mode)
        }
        return this;
    }
}