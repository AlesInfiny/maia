import { timeHandlers } from './handlers/time';
import { userHandlers } from './handlers/user';

export const handlers = [...timeHandlers, ...userHandlers];
