package org.firstinspires.ftc.team16909.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.team16909.hardware.FettucineHardware;
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
    String clawPosition = "closed";



    public void init()
    {
        hardware = new FettucineHardware();
        hardware.init(hardwareMap);
        buttonTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

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
        clawArm();
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
        if(gamepad2.right_trigger>0)
        {
            hardware.intakeMotor.setPower(1.0);
        }
        else
        {
            hardware.intakeMotor.setPower(0.0);
        }
    }

    public void launcher()
    {
        if(gamepad2.left_trigger>0)
        {
            hardware.launcherMotor.setPower(-1.0);
        }
        else
        {
            hardware.launcherMotor.setPower(0.0);
        }
    }




    public void claw()
    {

        if(gamepad2.triangle && clawPosition.equals("closed"))
        {
            hardware.clawServo.setPosition(0.0);
            clawPosition = "open";
        }
        else if(gamepad2.triangle && clawPosition.equals("open"))
        {
            hardware.clawServo.setPosition(0.33);
            clawPosition = "closed";
        }
    }

    public void clawArm()
    {
        // Mecanum drivecode
        // Driver
        double y = -gamepad2.left_stick_y; // Remember, this is reversed!

        hardware.clawMotor.setPower(y/2.5);
    }
}
