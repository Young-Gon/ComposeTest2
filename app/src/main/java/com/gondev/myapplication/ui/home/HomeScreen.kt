package com.gondev.myapplication.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.gondev.myapplication.model.data.ProductListResponse
import com.gondev.myapplication.model.data.ProductResponse
import com.gondev.myapplication.theme.brownish_grey
import com.gondev.myapplication.util.gifImageLoader
import com.gondev.statemanager.component.StateLayer
import com.gondev.statemanager.state.Status

@Composable
fun HomeScreen(homeViewModel: HomeViewModel = viewModel()) {
    val result by homeViewModel.productList.collectAsState()
    HomeContents(result)
}

@Composable
fun HomeContents(result: Status<ProductListResponse>) {
    StateLayer(status = result) { list ->
        requireNotNull(list)
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(
                items = list.products,
                key = { product ->
                    product.id
                }
            ) { product ->
                ProductItem(product)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductItem(product: ProductResponse) {
    Column {
        Box(/*contentAlignment = Alignment.TopEnd*/) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = product.image,
                    imageLoader = LocalContext.current.gifImageLoader,
                ),
                contentDescription = null,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .fillMaxWidth()
                    .sizeIn(maxHeight = 180.dp),
                contentScale = ContentScale.FillBounds
            )
            CompositionLocalProvider(
                LocalMinimumTouchTargetEnforcement provides false,
            ) {
                IconButton(onClick = {}, modifier = Modifier.align(Alignment.TopEnd)) {
                    Icon(
                        Icons.Outlined.Favorite,
                        contentDescription = "Favorite",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                }
            }

        }

        Row {
            if (product.price != product.actual_price)
                Text(
                    text = "${((product.actual_price - product.price).toFloat() * 100f / product.actual_price.toFloat()).toInt()}%",
                    color = Color.Red,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            Text(
                text = String.format("%,d", product.actual_price),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Text(
            text = product.name,
            fontSize = 11.sp,
            color = brownish_grey
        )

        Row {
            if (product.is_new) {
                Text(
                    text = "NEW",
                    fontSize = 10.sp,
                    modifier = Modifier
                        .border(1.dp, Color.Black)
                        .padding(2.dp)
                )
            }
            Text(text = "${product.sell_count}개 구매중",
                fontSize = 11.sp,
                color = brownish_grey)
        }
    }
}

@Composable
@Preview
fun ProductItemPreview() {
    Surface(color = Color.White) {

        ProductItem(product = ProductResponse(
            id = -1,
            name = "Product1",
            image = "imageURL",
            actual_price = 1200,
            price = 1000,
            is_new = true,
            sell_count = 120,
        ))
    }
}

@Preview
@Composable
fun HomeContentsPreview() {
    Surface(color = Color.White) {
        /*HomeContents(result = Status.success(ProductListResponse(
            products = listOf(
                ProductResponse(
                    id = 1,
                    name = "Product1",
                    image = "imageURL",
                    actual_price = 1200,
                    price = 1000,
                    is_new = true,
                    sell_count = 120,
                ),
                ProductResponse(
                    id = 2,
                    name = "Product1",
                    image = "imageURL",
                    actual_price = 1200,
                    price = 1000,
                    is_new = true,
                    sell_count = 120,
                ),
            )
        )))*/
        HomeContents(result = Status.loading(null))
    }
}