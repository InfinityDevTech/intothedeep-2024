package org.firstinspires.ftc.teamcode.freeWifi.RR;

import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.localization.ThreeTrackingWheelLocalizer;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.freeWifi.RR.util.Encoder;

import java.util.Arrays;
import java.util.List;

/*
 * Sample tracking wheel localizer implementation assuming the standard configuration:
 *
 *    /--------------\
 *    |     ____     |
 *    |     ----     |
 *    | ||        || |
 *    | ||        || |
 *    |              |
 *    |              |
 *    \--------------/
 *
 */
@Config
public class StandardTrackingWheelLocalizer extends ThreeTrackingWheelLocalizer {
    public static double TICKS_PER_REV = 2000;
    public static double WHEEL_RADIUS = 1.88976/2.0; // in
    public static double GEAR_RATIO = 1; // output (wheel) speed / input (encoder) speed

    public static double LATERAL_DISTANCE = 6.48; // in; distance between the left and right wheels

    // Formerly 3.5
    public static double FORWARD_OFFSET = -1.75; // in 1.625; offset of the lateral wheel

    public static double X_MULTIPLIER = 1; // Multiplier in the X direction
    public static double Y_MULTIPLIER = 1; // Multiplier in the Y direction

    private Encoder leftEncoder, rightEncoder, frontEncoder;

    public static Boolean FLIP_RIGHT = true;
    public static Boolean FLIP_LEFT = false;
    public static Boolean FLIP_FRONT = false;


    public static double LATERAL_OFFSET = 0.48; //0.5;
    public StandardTrackingWheelLocalizer(HardwareMap hardwareMap) {
        super(Arrays.asList(
                new Pose2d(0, (LATERAL_DISTANCE / 2)+LATERAL_OFFSET, 0), // left
                new Pose2d(0, -(LATERAL_DISTANCE / 2)+LATERAL_OFFSET, 0), // right
                new Pose2d(FORWARD_OFFSET, 0, Math.toRadians(90)) // front
        ));

        leftEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "front_right"));
        frontEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "front_left"));
        rightEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "back_left"));

        if (FLIP_RIGHT) {
            rightEncoder.setDirection(Encoder.Direction.REVERSE);
        } else {
            rightEncoder.setDirection(Encoder.Direction.FORWARD);
        }

        if (FLIP_LEFT) {
            leftEncoder.setDirection(Encoder.Direction.REVERSE);
        } else {
            leftEncoder.setDirection(Encoder.Direction.FORWARD);
        }

        if (FLIP_FRONT) {
            frontEncoder.setDirection(Encoder.Direction.REVERSE);
        } else {
            frontEncoder.setDirection(Encoder.Direction.FORWARD);
        }

        //rightEncoder.setDirection(Encoder.Direction.REVERSE);
        //leftEncoder.setDirection(Encoder.Direction.REVERSE);
    }

    public static double encoderTicksToInches(double ticks) {
        return WHEEL_RADIUS * 2 * Math.PI * GEAR_RATIO * ticks / TICKS_PER_REV;
    }

    @NonNull
    @Override
    public List<Double> getWheelPositions() {
        System.out.println("Left Enc: " + leftEncoder.getCurrentPosition());
        System.out.println("Right Enc: " + rightEncoder.getCurrentPosition());
        System.out.println("Front Enc: " + frontEncoder.getCurrentPosition());
        return Arrays.asList(
                encoderTicksToInches(leftEncoder.getCurrentPosition()) * X_MULTIPLIER,
                encoderTicksToInches(rightEncoder.getCurrentPosition()) * X_MULTIPLIER,
                encoderTicksToInches(frontEncoder.getCurrentPosition()) * Y_MULTIPLIER
        );
    }

    @NonNull
    @Override
    public List<Double> getWheelVelocities() {
        // TODO: If your encoder velocity can exceed 32767 counts / second (such as the REV Through Bore and other
        //  competing magnetic encoders), change Encoder.getRawVelocity() to Encoder.getCorrectedVelocity() to enable a
        //  compensation method

        return Arrays.asList(
                encoderTicksToInches(leftEncoder.getCorrectedVelocity()) * X_MULTIPLIER,
                encoderTicksToInches(rightEncoder.getCorrectedVelocity()) * X_MULTIPLIER,
                encoderTicksToInches(frontEncoder.getCorrectedVelocity()) * Y_MULTIPLIER
        );
    }
}
