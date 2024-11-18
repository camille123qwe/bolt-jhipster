import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Device from './device';
import DeviceDetail from './device-detail';
import DeviceUpdate from './device-update';
import DeviceDeleteDialog from './device-delete-dialog';

const DeviceRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Device />} />
    <Route path="new" element={<DeviceUpdate />} />
    <Route path=":id">
      <Route index element={<DeviceDetail />} />
      <Route path="edit" element={<DeviceUpdate />} />
      <Route path="delete" element={<DeviceDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DeviceRoutes;
