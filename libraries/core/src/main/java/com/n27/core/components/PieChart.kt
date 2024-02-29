import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

data class PieChartData(val slices: List<PieChartSlice>)

data class PieChartSlice(val value: Float, val color: String)

@Composable
fun PieChart(data: PieChartData) {
    var totalProportion = 0f

    data.slices.forEach { totalProportion += it.value }

    Column(Modifier.fillMaxWidth()) {
        Canvas(
            Modifier
                .aspectRatio(1f)
                .align(Alignment.CenterHorizontally)
        ) {
            var startAngle = 180f

            data.slices.forEach { slice ->
                val sweepAngle = 180f * (slice.value / totalProportion)

                drawArc(
                    Color(android.graphics.Color.parseColor(slice.color)),
                    startAngle,
                    sweepAngle,
                    useCenter = true,
                    size = size.copy(width = size.minDimension, height = size.minDimension),
                    topLeft = Offset(0f, 0f)
                )

                startAngle += sweepAngle
            }

            val holeRadius = size.minDimension * 0.3f

            drawCircle(
                color = Color.White,
                radius = holeRadius,
                center = center
            )
        }
    }
}
