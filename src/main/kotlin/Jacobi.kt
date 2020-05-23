import java.util.*
import kotlin.math.sqrt
import kotlin.system.measureTimeMillis

const val N = 1000
const val MAX_ITERATIONS = 20000
const val CONVERGENCE_THRESHOLD = 0.0001f
const val SQ_CONVERGENCE_THRESHOLD = CONVERGENCE_THRESHOLD * CONVERGENCE_THRESHOLD
const val SEED = 0L

fun run(A: FloatArray, b: FloatArray, _x: FloatArray, _xTmp: FloatArray): Int {
    var x = _x
    var xTmp = _xTmp

    var sqDiff: Float

    var itr = 0

    do {
        sqDiff = 0.0f

        repeat(N) { row ->
            var dot = 0.0f
            repeat(N) { col ->
                dot += A[row * N + col] * x[col]
            }
            xTmp[row] = (b[row] - dot) / A[row * N + row] + x[row]

            val diff = xTmp[row] - x[row]
            sqDiff += diff * diff
        }

        val temp = x
        x = xTmp
        xTmp = temp

        itr++
    } while ((itr < MAX_ITERATIONS) && (sqDiff > SQ_CONVERGENCE_THRESHOLD))

    return itr
}

fun main(args: Array<String>) {
    val random = Random(SEED)

    val A = FloatArray(N * N)
    val b = FloatArray(N) { random.nextFloat() }
    val x = FloatArray(N)
    val xTmp = FloatArray(N)

    repeat(N) { row ->
        var rowSum = 0.0f
        repeat(N) { col ->
            val value = random.nextFloat()
            A[row * N + col] = value
            rowSum += value
        }
        A[row + row * N] += rowSum
    }

    var itr: Int = -1

    val time = measureTimeMillis {
        itr = run(A, b, x, xTmp)
    }

    var err = 0.0
    repeat(N) { row ->
        var tmp = 0.0
        repeat(N) { col ->
            tmp += A[row * N + col] * x[col]
        }

        tmp = b[row] - tmp
        err += tmp * tmp
    }
    err = sqrt(err)

    println("Solution error = $err")
    println("Iterations     = $itr")
    println("Solver runtime = ${time / 1000.0}s")
    if (itr == MAX_ITERATIONS) {
        println("WARNING: solution did not converge")
    }
}
