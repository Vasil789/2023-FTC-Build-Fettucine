package org.firstinspires.ftc.team16909.hardware;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.internal.system.Assert;

public class FettucineHardware
{
    public DcMotorEx frontLeftMotor;
    public DcMotorEx frontRightMotor;
    public DcMotorEx backLeftMotor;
    public DcMotorEx backRightMotor;
    public DcMotorEx[] driveMotors;


    public void init(HardwareMap hardwareMap)
    {
        Assert.assertNotNull(hardwareMap);
        initializeDriveMotors(hardwareMap);
    }

    public void initializeDriveMotors(HardwareMap hardwareMap)
    {
        frontLeftMotor = hardwareMap.get(DcMotorEx.class, FettucineIds.LEFT_FRONT_MOTOR);
        frontRightMotor = hardwareMap.get(DcMotorEx.class, FettucineIds.RIGHT_FRONT_MOTOR);
        backLeftMotor = hardwareMap.get(DcMotorEx.class, FettucineIds.LEFT_REAR_MOTOR);
        backRightMotor = hardwareMap.get(DcMotorEx.class, FettucineIds.RIGHT_REAR_MOTOR);

        driveMotors = new DcMotorEx[] {frontLeftMotor,frontRightMotor,backLeftMotor,backRightMotor};

        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        backRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);


        for (DcMotorEx motor : driveMotors)
        {
            motor.setPower(0.0);
            motor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
            motor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        }

    }
}