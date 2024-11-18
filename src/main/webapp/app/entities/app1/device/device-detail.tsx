import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './device.reducer';

export const DeviceDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const deviceEntity = useAppSelector(state => state.app1.device.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="deviceDetailsHeading">
          <Translate contentKey="app1App.app1Device.detail.title">Device</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="app1App.app1Device.id">Id</Translate>
            </span>
          </dt>
          <dd>{deviceEntity.id}</dd>
          <dt>
            <span id="uuid">
              <Translate contentKey="app1App.app1Device.uuid">Uuid</Translate>
            </span>
          </dt>
          <dd>{deviceEntity.uuid}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="app1App.app1Device.name">Name</Translate>
            </span>
          </dt>
          <dd>{deviceEntity.name}</dd>
          <dt>
            <span id="label">
              <Translate contentKey="app1App.app1Device.label">Label</Translate>
            </span>
          </dt>
          <dd>{deviceEntity.label}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="app1App.app1Device.description">Description</Translate>
            </span>
          </dt>
          <dd>{deviceEntity.description}</dd>
          <dt>
            <span id="role">
              <Translate contentKey="app1App.app1Device.role">Role</Translate>
            </span>
          </dt>
          <dd>{deviceEntity.role}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="app1App.app1Device.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>{deviceEntity.createdAt ? <TextFormat value={deviceEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
        </dl>
        <Button tag={Link} to="/app1/device" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/app1/device/${deviceEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DeviceDetail;
