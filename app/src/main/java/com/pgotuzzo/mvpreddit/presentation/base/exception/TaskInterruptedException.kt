package com.pgotuzzo.mvpreddit.presentation.base.exception

import kotlinx.coroutines.CancellationException

class TaskInterruptedException : CancellationException()