import { registerPlugin } from '@capacitor/core';

import type { WallpaperHelperPlugin } from './definitions';

const WallpaperHelper = registerPlugin<WallpaperHelperPlugin>(
  'WallpaperHelper',
  {
    web: () => import('./web').then(m => new m.WallpaperHelperWeb()),
  },
);

export * from './definitions';
export { WallpaperHelper };
