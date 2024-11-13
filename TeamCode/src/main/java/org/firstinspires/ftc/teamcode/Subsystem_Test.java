package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

import java.util.Locale;


//this line identifies if this is an autonomous or teleop program
@Autonomous(name = "Subsystem_Test")
//this line allows you to modify variables inside the dashboard
@Config
public class Subsystem_Test extends OpMode {
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotorEx motor0;
    public static double power = 0.2;
    private GoBildaPinpointDriver odo;
    private double oldTime = 0;

    //this section allows us to access telemetry data from a browser
    FtcDashboard dashboard = FtcDashboard.getInstance();
    Telemetry dashboardTelemetry = dashboard.getTelemetry();

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        this.motor0 = hardwareMap.get(DcMotorEx.class,"backLeft");

        //this is taken from goBilda's gethub
        //https://github.com/goBILDA-Official/FtcRobotController-Add-Pinpoint/blob/goBILDA-Odometry-Driver/TeamCode/src/main/java/org/firstinspires/ftc/teamcode/SensorGoBildaPinpointExample.java
        this.odo = hardwareMap.get(GoBildaPinpointDriver.class,"odo");
        this.odo.setOffsets(-84.0, -168.0);
        this.odo.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_SWINGARM_POD);
        this.odo.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, GoBildaPinpointDriver.EncoderDirection.FORWARD);
        this.odo.resetPosAndIMU();






    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit START
     */
    @Override
    public void init_loop() {
        // Tell the driver that initialization is complete.
        dashboardTelemetry.addData("Status", "Initialized");
        dashboardTelemetry.addData("X offset", odo.getXOffset());
        dashboardTelemetry.addData("Y offset", odo.getYOffset());
        dashboardTelemetry.addData("Device Version Number:", odo.getDeviceVersion());
        dashboardTelemetry.addData("Device Scalar", odo.getYawScalar());
        dashboardTelemetry.update();
    }

    /*
     * Code to run ONCE when the driver hits START
     */
    @Override
    public void start() {
        runtime.reset();
        //testing2
        //testing3
    }

    /*
     * Code to run REPEATEDLY after the driver hits START but before they hit STOP
     */
    @Override
    public void loop() {
        /*
            Request an update from the Pinpoint odometry computer. This checks almost all outputs
            from the device in a single I2C read.
             */
        this.odo.update();

        //this.motor0.setPower(Subsystem_Test.power);
        Pose2D pos = odo.getPosition();
        String data = String.format(Locale.US, "{X: %.3f, Y: %.3f, H: %.3f}", pos.getX(DistanceUnit.MM), pos.getY(DistanceUnit.MM), pos.getHeading(AngleUnit.DEGREES));
        dashboardTelemetry.addData("Status", odo.getDeviceStatus());
        dashboardTelemetry.addData("Position", data);

        dashboardTelemetry.addData("motor0 power", motor0.getPower());
        dashboardTelemetry.addData("Status", "Run Time: " + runtime.toString());
        dashboardTelemetry.update();
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
        this.motor0.setPower(0);
        dashboardTelemetry.addData("motor0 power", motor0.getPower());
        dashboardTelemetry.update();
    }
}
