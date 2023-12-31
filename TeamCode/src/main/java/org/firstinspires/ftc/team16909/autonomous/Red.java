package org.firstinspires.ftc.team16909.autonomous;

import android.annotation.SuppressLint;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;
import org.firstinspires.ftc.team16909.drive.SampleMecanumDrive;
import org.firstinspires.ftc.team16909.hardware.FettucineHardware;
import org.firstinspires.ftc.team16909.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.team16909.autonomous.Utilities;

import com.qualcomm.robotcore.hardware.DcMotorSimple;


import java.util.ArrayList;


@Autonomous(name = "Fettucine Autonomous")
public class Red extends LinearOpMode {

    private SampleMecanumDrive drive;
    private Utilities util;
    private TrajectorySequence t1, t2, t3;
    private FettucineHardware hardware;

    OpenCvCamera camera;
    AprilTagDetectionPipeline aprilTagDetectionPipeline;

    // Lens intrinsics
    // UNITS ARE PIXELS
    // NOTE: this calibration is for the C920 webcam at 800x448.
    // You will need to do your own calibration for other configurations!
    double fx = 578.272;
    double fy = 578.272;
    double cx = 402.145;
    double cy = 221.506;

    static final double FEET_PER_METER = 3.28084;

    // UNITS ARE METERS
    double tagsize = 0.166;

    // Tag IDs
    int ID1 = 0;
    int ID2 = 1;
    int ID3 = 2;

    AprilTagDetection tagOfInterest = null;

    Pose2d startPose = new Pose2d(-60,12,Math.toRadians(90));

    @Override
    public void runOpMode() throws InterruptedException {

        FettucineHardware hardware = new FettucineHardware();
        util = new Utilities(hardware);

        drive = new SampleMecanumDrive(hardwareMap);
        drive.setPoseEstimate(new Pose2d(-60,12,Math.toRadians(90)));

        hardware.init(hardwareMap);

        hardware.frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        hardware.frontRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        hardware.backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        hardware.backRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        hardware.intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        hardware.launcherMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        hardware.clawMotor.setDirection(DcMotorSimple.Direction.FORWARD);


        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        aprilTagDetectionPipeline = new AprilTagDetectionPipeline(tagsize, fx, fy, cx, cy);

        camera.setPipeline(aprilTagDetectionPipeline);
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                camera.startStreaming(800, 448, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {

            }
        });


        while (!isStarted()) {
            ArrayList<AprilTagDetection> currentDetections = aprilTagDetectionPipeline.getLatestDetections();

            if (currentDetections.size() != 0) {
                boolean tagFound = false;

                for (AprilTagDetection tag : currentDetections) {
                    if (tag.id == ID1|| tag.id == ID2 || tag.id == ID3) {
                        tagOfInterest = tag;
                        tagFound = true;
                        break;
                    }
                }

                if (tagFound) {
                    telemetry.addLine("Tag of interest is in sight! :pog:\n\nLocation data:");
                    tagToTelemetry(tagOfInterest);
                } else {
                    telemetry.addLine("Don't see tag of interest :(");

                    if (tagOfInterest == null) {
                        telemetry.addLine("(The tag has never been seen)");
                    } else {
                        telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
                        tagToTelemetry(tagOfInterest);
                    }
                }

            } else {
                telemetry.addLine("Don't see tag of interest :(");

                if (tagOfInterest == null) {
                    telemetry.addLine("(The tag has never been seen)");
                } else {
                    telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
                    tagToTelemetry(tagOfInterest);
                }

            }

            telemetry.update();
            sleep(20);
        }

        /*
         * The START command just came in: now work off the latest snapshot acquired
         * during the init loop.
         */

        /* Update the telemetry */
        if (tagOfInterest != null) {
            telemetry.addLine("Tag snapshot:\n");
            tagToTelemetry(tagOfInterest);
            telemetry.update();
        } else {
            telemetry.addLine("No tag snapshot available, it was never sighted during the init loop :(");
            telemetry.update();
        }

        waitForStart();

        if (tagOfInterest.id == ID1)
        {
            buildTrajectories();
            drive.followTrajectorySequence(t1);
            util.shootDisk();
            drive.followTrajectorySequence(t2);


        }
        else if (tagOfInterest.id == ID2)
        {
            buildTrajectories();
            drive.followTrajectorySequence(t1);
            util.shootDisk();
            drive.followTrajectorySequence(t2);
        }
        else if (tagOfInterest.id == ID3)
        {
            buildTrajectories();
            drive.followTrajectorySequence(t1);
            util.shootDisk();
            drive.followTrajectorySequence(t2);
        }


    }

    public void buildTrajectories() {
        t1 = drive.trajectorySequenceBuilder(new Pose2d(-60,12,Math.toRadians(90)))
                .forward(90)
                .turn(Math.toRadians(-65))
                .forward(24)
                .turn(Math.toRadians(200))
                .build();

        t2 = drive.trajectorySequenceBuilder(t1.end())
                .turn(Math.toRadians(-30))
                .forward(48)
                .turn(Math.toRadians(-90))
                .forward(24)
                .build();


    }

    @SuppressLint("DefaultLocale")
    void tagToTelemetry(AprilTagDetection detection) {
        Orientation rotation = Orientation.getOrientation(detection.pose.R, AxesReference.INTRINSIC, AxesOrder.YXZ, AngleUnit.DEGREES);

        telemetry.addLine(String.format("\nDetected tag ID=%d", detection.id));
        telemetry.addLine(String.format("Translation X: %.2f feet", detection.pose.x * FEET_PER_METER));
        telemetry.addLine(String.format("Translation Y: %.2f feet", detection.pose.y * FEET_PER_METER));
        telemetry.addLine(String.format("Translation Z: %.2f feet", detection.pose.z * FEET_PER_METER));
        telemetry.addLine(String.format("Rotation Yaw: %.2f degrees", Math.toDegrees(rotation.firstAngle)));
        telemetry.addLine(String.format("Rotation Pitch: %.2f degrees", Math.toDegrees(rotation.secondAngle)));
        telemetry.addLine(String.format("Rotation Roll: %.2f degrees", Math.toDegrees(rotation.thirdAngle)));
    }





}
