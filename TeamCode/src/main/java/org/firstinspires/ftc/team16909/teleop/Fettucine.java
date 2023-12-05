package org.firstinspires.ftc.team16909.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.team16909.hardware.FettucineHardware;

import java.util.concurrent.TimeUnit;

@TeleOp(name = "Fettucine")
public class Fettucine extends OpMode
{
    //Initialization
    FettucineHardware hardware;
    final double FAST_SPEED = 1;
    final double MID_SPEED = .5;
    final double SLOW_SPEED = .2;
    double slowConstant = FAST_SPEED;
    ElapsedTime buttonTime = null;
    ElapsedTime launcherServoTime = null;
    String clawPosition = "closed";



    public void init()
    {
        hardware = new FettucineHardware();
        hardware.init(hardwareMap);
        buttonTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        launcherServoTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);


        telemetry.addData("Status","Initialized");
        telemetry.update();
    }

    public void start()
    {
        telemetry.addData("Status","Started");
        telemetry.update();
    }

    public void loop()
    {
        drive();
        intake();
        launcher();
        claw();

    }

    public void drive()
    {
        // Mecanum drivecode
        // Driver
        double y = -gamepad1.left_stick_y; // Remember, this is reversed!
        double x = gamepad1.left_stick_x; // Counteract imperfect strafing
        double rx = gamepad1.right_stick_x;

        double leftFrontPower = y + x + rx;
        double leftRearPower = y - x + rx;
        double rightFrontPower = y - x - rx;
        double rightRearPower = y + x - rx;

        if (Math.abs(leftFrontPower) > 1 || Math.abs(leftRearPower) > 1 ||
                Math.abs(rightFrontPower) > 1 || Math.abs(rightRearPower) > 1 )
        {
            // Find the largest power
            double max;
            max = Math.max(Math.abs(leftFrontPower), Math.abs(leftRearPower));
            max = Math.max(Math.abs(rightFrontPower), max);
            max = Math.max(Math.abs(rightRearPower), max);

            // Divide everything by max (it's positive so we don't need to worry
            // about signs)
            leftFrontPower /= max;
            leftRearPower /= max;
            rightFrontPower /= max;
            rightRearPower /= max;
        }

        if (gamepad1.dpad_up)
        {
            leftFrontPower = 1;
            rightRearPower = 1;
            rightFrontPower = 1;
            leftRearPower = 1;
        }
        else if(gamepad1.dpad_right)
        {
            leftFrontPower = 1;
            rightRearPower = 1;
            rightFrontPower = -1;
            leftRearPower = -1;
        }
        else if(gamepad1.dpad_left)
        {
            leftFrontPower = -1;
            rightRearPower = -1;
            rightFrontPower = 1;
            leftRearPower = 1;
        }
        else if (gamepad1.dpad_down )
        {
            leftFrontPower = -1;
            rightRearPower = -1;
            rightFrontPower = -1;
            leftRearPower = -1;
        }

        if (gamepad1.circle && (slowConstant == FAST_SPEED || slowConstant == MID_SPEED) && buttonTime.time() >= 500)
        {
            slowConstant = SLOW_SPEED;
            buttonTime.reset();
        }
        else if (gamepad1.square && (slowConstant == FAST_SPEED || slowConstant == SLOW_SPEED) && buttonTime.time() >= 500 )
        {
            slowConstant = MID_SPEED;
            buttonTime.reset();
        }
        else if (gamepad1.cross && (slowConstant == SLOW_SPEED || slowConstant == MID_SPEED) && buttonTime.time() >= 500)
        {
            slowConstant = FAST_SPEED;
            buttonTime.reset();
        }


        hardware.frontLeftMotor.setPower(leftFrontPower * slowConstant);
        hardware.backLeftMotor.setPower(leftRearPower * slowConstant);
        hardware.frontRightMotor.setPower(rightFrontPower * slowConstant);
        hardware.backRightMotor.setPower(rightRearPower * slowConstant);
    }


    public void intake()
    {
        double intakeValue = -gamepad2.right_stick_y; // Remember, this is reversed!

        hardware.intakeMotor.setPower(intakeValue);
    }

    public void launcher()
    {
        // Launcher Motor Code
        if(gamepad2.left_trigger>0)
        {
            hardware.launcherMotor.setPower(-1.0);
        }
        else if(gamepad2.left_trigger == 0)
        {
            hardware.launcherMotor.setPower(0.0);
        }

        // Code for Servo that pushes the rings towards the motor
        if(gamepad2.dpad_up)
        {   launcherServoTime.reset();
            hardware.launcherServo.setPosition(0.8);
        }
        if(launcherServoTime.time()>2000)
            hardware.launcherServo.setPosition(0.4);

    }





    public void claw()
    {

        // Code for Claw Servo
        if(gamepad2.right_bumper)
        {
            hardware.clawServo.setDirection(Servo.Direction.FORWARD);
            hardware.clawServo.setPosition(0.3);
            clawPosition = "open";
        }
        else if(gamepad2.left_bumper)
        {
            hardware.clawServo.setPosition(0.9);
            clawPosition = "closed";
        }

        // Code for Claw Arm motor
        double y = -gamepad2.left_stick_y; // Remember, this is reversed!

        hardware.clawMotor.setPower(y/2);

    }

}
