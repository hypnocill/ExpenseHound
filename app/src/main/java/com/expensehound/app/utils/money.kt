import com.expensehound.app.data.entity.Currency
import java.text.DecimalFormat

val moneyFormat = DecimalFormat("#.##")

fun formatPrice(price: Double, currency: Currency): String {
    return moneyFormat.format(price) + currency
}