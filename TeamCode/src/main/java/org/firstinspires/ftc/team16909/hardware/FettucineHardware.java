package org.firstinspires.ftc.team16909.hardware;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
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
    public DcMotorEx intakeMotor;
    public DcMotorEx launcherMotor;
    public DcMotorEx clawMotor;
    public Servo clawServo;
    public Servo launcherServo;


    public void init(HardwareMap hardwareMap)
    {
        Assert.assertNotNull(hardwareMap);
        initializeDriveMotors(hardwareMap);
        initializeIntakeMotor(hardwareMap);
        initializeLauncher(hardwareMap);
        initializeClaw(hardwareMap);
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
    public void initializeIntakeMotor(HardwareMap hardwareMap)
    {
        
        intakeMotor = hardwareMap.get(DcMotorEx.class, FettucineIds.INTAKE_MOTOR);
        intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        
        intakeMotor.setPower(0.0);
        intakeMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        intakeMotor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void initializeLauncher(HardwareMap hardwareMap)
    {
        launcherMotor = hardwareMap.get(DcMotorEx.class, FettucineIds.LAUNCHER_MOTOR);
        launcherMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        launcherMotor.setPower(0.0);
        launcherMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        launcherMotor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

        launcherServo = hardwareMap.get(Servo.class, FettucineIds.LAUNCHER_SERVO);
        launcherServo.setPosition(0.0);
    }


    public void initializeClaw(HardwareMap hardwareMap)
    {
        clawServo = hardwareMap.get(Servo.class, FettucineIds.CLAW_SERVO);
        clawServo.setPosition(0.0);

        clawMotor = hardwareMap.get(DcMotorEx.class, FettucineIds.CLAW_MOTOR);
        clawMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        clawMotor.setPower(0.0);
        clawMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        clawMotor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);


    }

}
