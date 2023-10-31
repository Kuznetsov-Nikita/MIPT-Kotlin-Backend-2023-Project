package api.utils

import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.util.pipeline.PipelineContext


fun PipelineContext<*, ApplicationCall>.getPathParameter(name: String): String? {
    return call.parameters[name]
}
