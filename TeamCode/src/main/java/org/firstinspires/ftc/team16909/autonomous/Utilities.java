package org.firstinspires.ftc.team16909.autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.team16909.hardware.FettucineHardware;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Utilities
{
    FettucineHardware hardware;
    ElapsedTime time = new ElapsedTime();

    public Utilities(FettucineHardware hardware)
    {
        this.hardware = hardware;
        resetEncoderModes(hardware);
    }
    public void resetEncoderModes(FettucineHardware hardware)
    {
        for (DcMotorEx motor : hardware.driveMotors)
        {
            motor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        }

        hardware.intakeMotor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        hardware.launcherMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    }

    public void wait(int millis, Telemetry telemetry)
    {
        ElapsedTime waitTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        while (waitTime.time() < millis)
        {
            telemetry.addData("Status", "Waiting");
            telemetry.addData("Time Left", "" + (millis - waitTime.time()));
            telemetry.update();
        }
    }

    public void wait(int millis)
    {
        ElapsedTime waitTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        while (waitTime.time() < millis)
        {
            continue;
        }
    }
    public void intake(int ms)
    {
        hardware.intakeMotor.setPower(1.0);
        this.wait(ms);
        hardware.intakeMotor.setPower(0.0);
    }

    public void shootDisk()
    {
        hardware.launcherMotor.setPower(-1.0);
        this.wait(2500);
        hardware.launcherServo.setPosition(1);
        this.wait(1500);
        hardware.launcherServo.setPosition(0.0);
        hardware.launcherMotor.setPower(0.0);
    }

}
