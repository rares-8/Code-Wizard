package com.example.codewizard.ui.wizard_chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.codewizard.R
import com.example.codewizard.data.Project
import com.example.codewizard.navigation.NavigationDestination
import dev.jeziellago.compose.markdowntext.MarkdownText

object ChatDestination : NavigationDestination {
    override val route = "chat"
    override val titleRes = R.string.app_name
}

@Composable
fun YourSpeechBubble(message: String, modifier: Modifier = Modifier) {
    TextSpeechBubble(
        stringResource(R.string.you),
        message,
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.onPrimary,
        SHAPE_OF_YOU,
        Alignment.End,
        modifier
    )
}

@Composable
fun GeminiSpeechBubble(message: String, modifier: Modifier = Modifier) {
    TextSpeechBubble(
        stringResource(R.string.gemini),
        message,
        MaterialTheme.colorScheme.tertiary,
        MaterialTheme.colorScheme.onTertiary,
        SHAPE_OF_GEMINI,
        Alignment.Start,
        modifier
    )
}

@Composable
fun GeminiIsThinking(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentWidth()
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(32.dp),
            color = MaterialTheme.colorScheme.tertiary,
            trackColor = MaterialTheme.colorScheme.tertiaryContainer,
        )
    }
}

@Composable
fun TextSpeechBubble(
    author: String,
    message: String,
    containerColor: androidx.compose.ui.graphics.Color,
    contentColor: androidx.compose.ui.graphics.Color,
    shape: RoundedCornerShape,
    alignment: Alignment.Horizontal,
    modifier: Modifier = Modifier
) {
    SpeechBubble(
        author,
        containerColor,
        contentColor,
        shape,
        alignment,
        modifier
    ) {
        MarkdownText(
            markdown = message,
            modifier = Modifier
                .padding(12.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun SpeechBubble(
    author: String,
    containerColor: Color,
    contentColor: Color,
    shape: RoundedCornerShape,
    alignment: Alignment.Horizontal,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentWidth(align = alignment)
            .fillMaxWidth(0.9f)
    ) {
        Text(
            text = author,
            fontSize = 18.sp,
            color = containerColor,
            modifier = Modifier
                .align(alignment = alignment)
                .padding(vertical = 4.dp),
            style = MaterialTheme.typography.titleLarge
        )
        Card(
            shape = shape,
            colors = CardDefaults.cardColors(
                containerColor = containerColor,
                contentColor = contentColor
            ),
            modifier = Modifier.align(alignment = Alignment.End)
        ) {
            content()
        }
    }
}

val SHAPE_OF_YOU = RoundedCornerShape(
    topStart = 16.dp,
    bottomEnd = 16.dp,
    bottomStart = 16.dp
)

val SHAPE_OF_GEMINI = RoundedCornerShape(
    topEnd = 16.dp,
    bottomEnd = 16.dp,
    bottomStart = 16.dp
)

@Composable
fun Chat(messages: List<Message>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        reverseLayout = true
    ) {
        // We use reverse layout and a reversed list to trick Compose to always scroll to the bottom
        items(messages.reversed()) { message ->
            when (message) {
                is Message.TextFromYou -> YourSpeechBubble(message.content)
                is Message.TextFromGemini -> GeminiSpeechBubble(message.content)
                is Message.GeminiIsThinking -> GeminiIsThinking()
            }
        }
    }
}

@Composable
fun BottomBar(onSubmit: (String) -> Unit, modifier: Modifier = Modifier) {
    var prompt by remember { mutableStateOf("") }

    Row(modifier = modifier.background(MaterialTheme.colorScheme.primaryContainer)) {
        TextField(
            value = prompt,
            placeholder = {
                Text(
                    text = stringResource(R.string.query_placeholder),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            },
            onValueChange = { prompt = it },
            modifier = Modifier.weight(1f),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer
            ),
        )
        IconButton(
            onClick = {
                onSubmit(prompt)
                prompt = ""
            },
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = stringResource(R.string.send_message),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
fun ChatScreen(
    topBar: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChatViewModel = viewModel()
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val state = viewModel.uiState.collectAsState()

    Scaffold(
        topBar = topBar,
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Chat(
                messages = state.value.messages,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 4.dp)
            )
            BottomBar(
                onSubmit = {
                    viewModel.onNewMessageFromUser(it)
                    keyboardController?.hide()
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

}