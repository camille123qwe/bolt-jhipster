import React from 'react';
import { Route } from 'react-router-dom';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import { combineReducers, ReducersMapObject } from '@reduxjs/toolkit';

import getStore from 'app/config/store';

import entitiesReducers from './reducers';

import Project from './app1/project';
import Device from './app1/device';
import Strategy from './app1/strategy';
import ExecutionType from './app1/execution-type';
import Source from './app1/source';
import Customer from './app1/customer';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  const store = getStore();
  store.injectReducer('app1', combineReducers(entitiesReducers as ReducersMapObject));
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="/project/*" element={<Project />} />
        <Route path="/device/*" element={<Device />} />
        <Route path="/strategy/*" element={<Strategy />} />
        <Route path="/execution-type/*" element={<ExecutionType />} />
        <Route path="/source/*" element={<Source />} />
        <Route path="/customer/*" element={<Customer />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
