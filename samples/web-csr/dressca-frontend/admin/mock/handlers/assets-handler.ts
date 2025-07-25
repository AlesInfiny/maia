import { HttpStatusCode } from 'axios'
import { HttpResponse, http } from 'msw'

export const assetsHandlers = [
  http.get('/api/assets/:assetCode', async ({ params }) => {
    const imageBuffer = await fetch(`/mock/images/${params.assetCode?.toString()}.png`).then(
      (response) => response.arrayBuffer(),
    )
    return HttpResponse.arrayBuffer(imageBuffer, {
      status: HttpStatusCode.Ok,
      headers: {
        'Content-Type': 'image/png',
      },
    })
  }),
]
