import { HttpResponse, http } from 'msw';

export const assetsHandlers = [
  http.get('/api/assets/:assetCode', async ({ params }) => {
    const imageBuffer = await fetch(
      `/mock/images/${params.assetCode}.png`,
    ).then((response) => response.arrayBuffer());
    return HttpResponse.arrayBuffer(imageBuffer, {
      status: 200,
      headers: {
        'Content-Type': 'image/png',
      },
    });
  }),
];
