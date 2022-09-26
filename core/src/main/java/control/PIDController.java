package control;

public class PIDController {
    private double kP = 1;
    public double getKP() { return kP; }
    public void setKP(double kP) { this.kP = kP; }

    private double kI = 0;
    public double getKI() { return kI; }
    public void setKI(double kI) { this.kI = kI; }

    private double kD = 0;
    public double getKD() { return kD; }
    public void setKD(double kD) { this.kD = kD; }

    private double kF = 1;
    public double getKF() { return kF; }
    public void setKF(double kF) { this.kF = kF; }


    private PIDCoefficients.FeedForwardProvider F = (double t, double c, long time) -> 0;
    public void setF(PIDCoefficients.FeedForwardProvider f) {
        this.F = f;
    }

    /**
     * Create a PID Controller with default constants. Ideal for testing constants using the getters and setters.
     * @see #setKP(double)
     * @see #setKI(double)
     * @see #setKD(double)
     * @see #setKF(double)
     * @see #setF(PIDCoefficients.FeedForwardProvider)
     */
    public PIDController(){};

    /**
     * Create a PIDF controller with constants bundle. Ideal for production-ready code.
     * @param pidCoefficients
     * @see PIDCoefficients
     */
    public PIDController(PIDCoefficients pidCoefficients){
        this.kP = pidCoefficients.getKP();
        this.kI = pidCoefficients.getKI();
        this.kD = pidCoefficients.getKD();
        this.kF = pidCoefficients.getKF();
        this.F = pidCoefficients.getF();
    }

    /**
     * Create a PID Controller with given constant values.
     * @param kP
     * @param kI
     * @param kD
     */
    public PIDController(double kP, double kI, double kD){
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
    }

    /**
     * Create a PIDF Controller with given constant values.
     * @param kP
     * @param kI
     * @param kD
     * @param kF
     * @param F
     */
    public PIDController(double kP, double kI, double kD, double kF, PIDCoefficients.FeedForwardProvider F){
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.kF = kF;
        this.F = F;
    }

    private long lastKnownTime = 0;
    private double lastKnownError = 0;
    private double lastKnownInput = 0;

    public void setTarget(double target) {
        this.target = target;
        reset();
    }

    private double target = 0;

    public double getIntegral() {
        return integral;
    }

    public double getDerivative() {
        return derivative;
    }

    private double integral = 0;
    private double derivative = 0;

    /**
     * Resets the last known time, input, error, and integral of the PID controller. This method is internally called when setting a new static target.
     * @see #setTarget(double)
     */
    public void reset() {
        lastKnownTime = System.nanoTime() / (long)1E9;
        lastKnownError = target - lastKnownInput;
        integral = 0;
    }

    /**
     * Calculate function for a changing target.
     * @param target Current target
     * @param current Current state of the system
     * @see #calculate(double)
     */
    public void calculate(double target, double current){
        setTarget(target);
        calculate(current);
    }

    /**
     * Calculates the current error, integral, and derivative of the PID controller and caches results.
     * @param current current state of the system
     * @see #getOutput()
     */
    public void calculate(double current){
        double error = target-current;

        //Integral
        double currentTime = System.nanoTime() / (long) 1E9;
        double deltaTime =  currentTime - lastKnownTime;
        lastKnownTime = (long)currentTime;
        integral += ((0.5 * (lastKnownError + error))*deltaTime); //trapezoidal riemann sum

        //Derivative
        double deltaError = lastKnownError - error;
        derivative = deltaError / deltaTime;
        lastKnownError = error;
    }

    /**
     * Retrieves the value of the PID Controller with adjustments from coefficients.
     * @return Value of the PID controller. It's recommended to pass this to a scaling or clamping function before applying it to the system.
     */
    public double getOutput(){
        return kP * lastKnownError + kI * integral + kD * derivative + kF * F.apply(target, lastKnownInput, lastKnownTime);
    }
}
