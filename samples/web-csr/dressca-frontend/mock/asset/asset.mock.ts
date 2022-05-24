const base = 'api/assets';
import * as fs from 'fs';
import * as path from 'path';

const readFile = (file, response) => {
  fs.readFile(path.resolve(__dirname, file), (errors, data) => {
    response.end(data);
  });
};

export const assetMock = (middlewares) => {
  middlewares.use(`/${base}/b52dc7f712d94ca5812dd995bf926c04`, (_, res) => {
    res.writeHead(200, { 'Content-Type': 'image/png' });
    readFile(`b52dc7f712d94ca5812dd995bf926c04`, res);
  });
  middlewares.use(`/${base}/80bc8e167ccb4543b2f9d51913073492`, (_, res) => {
    res.writeHead(200, { 'Content-Type': 'image/png' });
    readFile(`80bc8e167ccb4543b2f9d51913073492`, res);
  });
  middlewares.use(`/${base}/05d38fad5693422c8a27dd5b14070ec8`, (_, res) => {
    res.writeHead(200, { 'Content-Type': 'image/png' });
    readFile(`05d38fad5693422c8a27dd5b14070ec8`, res);
  });
  middlewares.use(`/${base}/45c22ba3da064391baac91341067ffe9`, (_, res) => {
    res.writeHead(200, { 'Content-Type': 'image/png' });
    readFile(`45c22ba3da064391baac91341067ffe9`, res);
  });
  middlewares.use(`/${base}/4aed07c4ed5d45a5b97f11acedfbb601`, (_, res) => {
    res.writeHead(200, { 'Content-Type': 'image/png' });
    readFile(`4aed07c4ed5d45a5b97f11acedfbb601`, res);
  });
  middlewares.use(`/${base}/082b37439ecc44919626ba00fc60ee85`, (_, res) => {
    res.writeHead(200, { 'Content-Type': 'image/png' });
    readFile(`082b37439ecc44919626ba00fc60ee85`, res);
  });
  middlewares.use(`/${base}/f5f89954281747fa878129c29e1e0f83`, (_, res) => {
    res.writeHead(200, { 'Content-Type': 'image/png' });
    readFile(`f5f89954281747fa878129c29e1e0f83`, res);
  });
  middlewares.use(`/${base}/a8291ef2e8e14869a7048e272915f33c`, (_, res) => {
    res.writeHead(200, { 'Content-Type': 'image/png' });
    readFile(`a8291ef2e8e14869a7048e272915f33c`, res);
  });
  middlewares.use(`/${base}/66237018c769478a90037bd877f5fba1`, (_, res) => {
    res.writeHead(200, { 'Content-Type': 'image/png' });
    readFile(`66237018c769478a90037bd877f5fba1`, res);
  });
  middlewares.use(`/${base}/d136d4c81b86478990984dcafbf08244`, (_, res) => {
    res.writeHead(200, { 'Content-Type': 'image/png' });
    readFile(`d136d4c81b86478990984dcafbf08244`, res);
  });
  middlewares.use(`/${base}/47183f32f6584d7fb661f9216e11318b`, (_, res) => {
    res.writeHead(200, { 'Content-Type': 'image/png' });
    readFile(`47183f32f6584d7fb661f9216e11318b`, res);
  });
  middlewares.use(`/${base}/cf151206efd344e1b86854f4aa49fdef`, (_, res) => {
    res.writeHead(200, { 'Content-Type': 'image/png' });
    readFile(`cf151206efd344e1b86854f4aa49fdef`, res);
  });
  middlewares.use(`/${base}/ab2e78eb7fe3408aadbf1e17a9945a8c`, (_, res) => {
    res.writeHead(200, { 'Content-Type': 'image/png' });
    readFile(`ab2e78eb7fe3408aadbf1e17a9945a8c`, res);
  });
  middlewares.use(`/${base}/0e557e96bc054f10bc91c27405a83e85`, (_, res) => {
    res.writeHead(200, { 'Content-Type': 'image/png' });
    readFile(`0e557e96bc054f10bc91c27405a83e85`, res);
  });
  middlewares.use(`/${base}/e622b0098808492cb883831c05486b58`, (_, res) => {
    res.writeHead(200, { 'Content-Type': 'image/png' });
    readFile(`e622b0098808492cb883831c05486b58`, res);
  });
  middlewares.use(`/${base}`, (_, res) => {
    res.writeHead(404, { 'Content-Type': 'image/png' });
    res.end();
  });
};
